package com.fpt.fis.template.repository.helper;

import com.fpt.fis.template.model.FilterDTO;
import com.fpt.fis.template.model.enums.FilterConjunctionType;
import com.fpt.fis.template.model.enums.FilterDataType;
import com.fpt.fis.template.model.enums.FilterOperatorEnum;
import com.fpt.framework.security.support.AuthenticationContext;
import com.fpt.framework.web.api.exception.InvalidParameterException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
public class FilterConditionHelper {
    private static final Pattern filterAndRegex = Pattern.compile("and\\((?<conditions>.+)\\)");
    private static final Pattern filterOrRegex = Pattern.compile("or\\((?<conditions>.+)\\)");
    private static final Pattern operandRegex = Pattern
            .compile("(?<attPath>[\\w\\.]+)(:(?<dataType>[\\w]+))?\\|(?<operator>[\\w]+)\\|(?<value>[^\\|]*((?=,)|$))");
    private static final Pattern verticalSlashRegex = Pattern.compile("\\|");
    private static final Pattern currentUserRegex = Pattern.compile("currentUser\\(\\)", Pattern.MULTILINE);
    private static final Pattern oneLevelConjunctionRegex = Pattern
            .compile("(?<operator>(and|or))\\s*\\(\\s*(?!([^()]*and\\(|[^()]*or\\())(?<content>[^\\(\\)]+)\\)");

    public static List<FilterDTO> convertToAndFilter(String filterString) {
        Matcher match = filterAndRegex.matcher(filterString);
        if (StringUtils.isBlank(filterString)) {
            return new ArrayList<>();
        } else if (!match.find()) {
            throw new IllegalArgumentException("Invalid format of filter string: ");
        }
        return convertToFilter(match);
    }

    public static List<FilterDTO> convertToOrFilter(String filterString) {
        Matcher match = filterOrRegex.matcher(filterString);
        if (StringUtils.isBlank(filterString)) {
            return new ArrayList<>();
        } else if (!match.find()) {
            throw new IllegalArgumentException("Invalid format of filter string: ");
        }
        return convertToFilter(match);
    }

    private static List<FilterDTO> convertToFilter(Matcher match) {
        String conditionsString = match.group("conditions");
        long numberOfVSlash = verticalSlashRegex.matcher(conditionsString).results().count();
        if (numberOfVSlash % 2 == 1) {
            throw new IllegalArgumentException("Invalid format of at least 1 operand ");
        }
        // check for filter condition pattern
        List<FilterDTO> conditions = new ArrayList<>();
        Matcher operandMatcher = operandRegex.matcher(conditionsString);
        while (operandMatcher.find()) {
            // Prepare filter condition
            FilterDTO filterDto = FilterDTO.builder().field(operandMatcher.group("attPath"))
                    .value(operandMatcher.group("value"))
                    .dataType(FilterDataType.fromValue(operandMatcher.group("dataType")))
                    .operator(FilterOperatorEnum.fromValue(operandMatcher.group("operator"))).build();
            conditions.add(filterDto);
        }
        return conditions;
    }

    private static FilterDTO preProcessFilter(String fql) {
        log.debug("START preProcessFilter with fql: {}", fql);
        String tmpFql = fql;
        int count = 0;
        int exitLoopCount = 0;
        FilterDTO finalResult = null;
        Map<String, FilterDTO> conjunctionMapping = new HashMap<>();
        /**
         * Loop until there's no 'and(' or 'or(' left. Each loop remove one-level
         * conjunction with placeholder, keep one-level conjunction in mapping then
         * using later.
         **/
        while ((tmpFql.contains("and(") || tmpFql.contains("or(")) && exitLoopCount < 3) {
            /**
             * Create matcher for One-level conjection for remain fql string. tmpFql will be
             * shorter each loop run.
             */
            Matcher matcher = oneLevelConjunctionRegex.matcher(tmpFql);
            if (StringUtils.equals(fql, tmpFql)) {
                exitLoopCount++;
            }
            while (matcher.find()) {
                String matchedString = matcher.group();
                String tmpKey = "";
                FilterDTO tmpFilter = new FilterDTO();
                tmpFilter.setFilterString(matchedString);
                tmpFilter.setSubFilters(new ArrayList<>());
                count++;

                /**
                 * Read current matched String with format: and(xxx) | or(xxx) (X does not
                 * contain any other and|or operator)
                 */
                for (Map.Entry<String, FilterDTO> entry : conjunctionMapping.entrySet()) {
                    String key = entry.getKey();
                    FilterDTO value = entry.getValue();
                    if (matchedString.contains(key)) {
                        matchedString = matchedString.replaceAll(String.format("(,%s|%s)", key, key), "");
                        tmpFilter.getSubFilters().add(value);
                    }
                }

                /**
                 * Convert from matchedString with ("and" conjunction) or ("or" conjunction) to
                 * list FilterDTO with FilterConditionHelper class
                 */
                if (matchedString.startsWith("and")) {
                    tmpFilter.getSubFilters().addAll(FilterConditionHelper.convertToAndFilter(matchedString));
                    tmpFilter.setConjunction(FilterConjunctionType.AND);
                    tmpKey = String.format("#and%d", count);
                } else {
                    tmpFilter.getSubFilters().addAll(FilterConditionHelper.convertToOrFilter(matchedString));
                    tmpFilter.setConjunction(FilterConjunctionType.OR);
                    tmpKey = String.format("#or%d", count);
                }

                /**
                 * conjunctionMapping keep all convertedFilter with placeholder in matchedString
                 * placeholder will have format of (#or%d | #and%d) for example: #and1, #or2
                 */
                conjunctionMapping.put(tmpKey, tmpFilter);
                tmpFql = matcher.replaceFirst(tmpKey);

                /**
                 * Until last loop, tmpFql will have only 1 placeholder left: (#or%d | #and%d).
                 */
                finalResult = tmpFilter;
            }
        }
        log.debug("END preProcessFilter with fql: {}", fql);
        return finalResult;
    }

    public static Mono<FilterDTO> convertToMonoFilter(String filterString) {
        return replaceSpecialData(filterString).map(FilterConditionHelper::preProcessFilter)
                .onErrorResume(throwable -> Mono.error(new InvalidParameterException("Invalid Filter String", filterString)));
    }

    private static Mono<String> replaceSpecialData(String fql) {
        return AuthenticationContext.currentUserPrincipal().map(user -> {
            String userEmail = user.getUniqueName();
            Matcher matcher = currentUserRegex.matcher(fql);
            if (matcher.find()) {
                return matcher.replaceAll(userEmail);
            }
            return fql;
        });
    }
}
