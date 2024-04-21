package com.fpt.fis.template.repository.helper;

import com.fpt.fis.template.model.FilterDTO;
import com.fpt.fis.template.model.enums.FilterConjunctionType;
import com.fpt.fis.template.model.enums.FilterOperatorEnum;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
public class GenericFilterCriteriaBuilder {

    private static final Map<String, Function<FilterDTO, Criteria>> FILTER_CRITERIA = new HashMap<>();

    static {
        FILTER_CRITERIA.put(FilterOperatorEnum.EQUAL.name(),
                condition -> Criteria.where(condition.getField()).is(getValueWithDataType(condition)));
        FILTER_CRITERIA.put(FilterOperatorEnum.NOT_EQUAL.name(),
                condition -> Criteria.where(condition.getField()).ne(getValueWithDataType(condition)));
        FILTER_CRITERIA.put(FilterOperatorEnum.GREATER_THAN.name(),
                condition -> Criteria.where(condition.getField()).gt(getValueWithDataType(condition)));
        FILTER_CRITERIA.put(FilterOperatorEnum.LESS_THAN.name(),
                condition -> Criteria.where(condition.getField()).lt(getValueWithDataType(condition)));
        FILTER_CRITERIA.put(FilterOperatorEnum.GREATER_THAN_EQUALS.name(),
                condition -> Criteria.where(condition.getField()).gte(getValueWithDataType(condition)));
        FILTER_CRITERIA.put(FilterOperatorEnum.LESS_THAN_EQUALS.name(),
                condition -> Criteria.where(condition.getField()).lte(getValueWithDataType(condition)));
        FILTER_CRITERIA.put(FilterOperatorEnum.IN.name(), condition -> {
            Collection<Object> listToSearch = Stream.of(String.valueOf(condition.getValue()).split(","))
                    .map(item -> getValueWithDataType(
                            FilterDTO.builder().dataType(condition.getDataType()).value(item).build()))
                    .collect(Collectors.toList());
            return Criteria.where(condition.getField()).in(listToSearch);
        });
        FILTER_CRITERIA.put(FilterOperatorEnum.CONTAINS.name(), condition -> {
            Collection<Object> listToContain = Stream.of(String.valueOf(condition.getValue()).split(","))
                    .map(item -> getValueWithDataType(
                            FilterDTO.builder().dataType(condition.getDataType()).value(item).build()))
                    .collect(Collectors.toList());
            return Criteria.where(condition.getField()).all(listToContain);
        });
        FILTER_CRITERIA.put(FilterOperatorEnum.LIKE.name(), condition -> Criteria.where(condition.getField())
                .regex(String.valueOf(getValueWithDataType(condition)), "m"));
        FILTER_CRITERIA.put(FilterOperatorEnum.EXIST.name(), condition -> Criteria.where(condition.getField())
                .exists((boolean) getValueWithDataType(condition)));
    }

    public static Criteria convertConditionToCriteria(FilterDTO conditions, List<String> excludeAttributes) {
        log.debug("START convertConditionToCriteria with fql: {}", conditions.getFilterString());
        List<Criteria> results = GenericFilterCriteriaBuilder.convertConditionToCriteriaRecursively(conditions,
                excludeAttributes);
        log.debug("END convertConditionToCriteria with fql");
        if (!results.isEmpty()) {
            return results.get(0);
        }
        return new Criteria();
    }

    public static Criteria convertConditionToCriteria(FilterDTO conditions) {
        return GenericFilterCriteriaBuilder.convertConditionToCriteria(conditions, new ArrayList<>());
    }

    private static List<Criteria> convertConditionToCriteriaRecursively(FilterDTO conditions,
                                                                        List<String> excludeAttributes) {
        List<Criteria> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(conditions.getSubFilters()) && conditions.getConjunction() == null) {
            Criteria builtCriteria = GenericFilterCriteriaBuilder.buildCriteria(conditions);
            if (!excludeAttributes.isEmpty() && excludeAttributes.contains(conditions.getField())) {
                return List.of(new Criteria());
            }
            return List.of(builtCriteria);
        }

        if (conditions.getConjunction() != null) {
            List<Criteria> convertedList = new ArrayList<>();
            Criteria criteria = new Criteria();
            for (FilterDTO filterDto : conditions.getSubFilters()) {
                convertedList.addAll(GenericFilterCriteriaBuilder.convertConditionToCriteriaRecursively(filterDto,
                        excludeAttributes));
            }
            if (conditions.getConjunction() == FilterConjunctionType.AND) {
                criteria = criteria.andOperator(convertedList);
            } else {
                criteria = criteria.orOperator(convertedList);
            }
            result.add(criteria);
        }

        return result;
    }

    private static Criteria buildCriteria(FilterDTO condition) {
        Function<FilterDTO, Criteria> function = FILTER_CRITERIA.get(condition.getOperator().name());
        if (function == null) {
            throw new IllegalArgumentException("Invalid function param type: ");
        }
        return function.apply(condition);
    }

    private static Object getValueWithDataType(FilterDTO filterDto) {
        Function<FilterDTO, Object> function = filterDto.getDataType().getHandleMethod();
        return function.apply(filterDto);
    }
}
