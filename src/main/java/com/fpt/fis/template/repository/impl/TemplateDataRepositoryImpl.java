package com.fpt.fis.template.repository.impl;

import com.fpt.fis.template.model.FilterDTO;
import com.fpt.fis.template.model.response.TemplateListFilterResponse;
import com.fpt.fis.template.model.response.TemplateListResponse;
import com.fpt.fis.template.repository.TemplateDataRepository;
import com.fpt.fis.template.repository.entity.Template;
import com.fpt.fis.template.repository.helper.GenericFilterCriteriaBuilder;
import com.fpt.framework.security.support.data.nonrelation.query.ReactiveNonRelationPermissionQuery;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public class TemplateDataRepositoryImpl implements TemplateDataRepository {

    private final ReactiveMongoTemplate mongoTemplate;
    private final ReactiveNonRelationPermissionQuery permissionQuery;

    public TemplateDataRepositoryImpl(ApplicationContext applicationContext, ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        this.permissionQuery = applicationContext.getBean(ReactiveNonRelationPermissionQuery.class);
    }

    @Override
    public Mono<TemplateListFilterResponse> findWithFilter(FilterDTO filter, Pageable pageable) {
        return permissionQuery.buildPermissionCriteria().flatMap(pc -> {
            Criteria criteria = new Criteria();
            Criteria filterCriteria = GenericFilterCriteriaBuilder.convertConditionToCriteria(filter);
            List<Criteria> lc = new ArrayList<>();
            if (!pc.isEmpty()) {
                Criteria permissionCriteria = new Criteria().orOperator(pc);
                lc.add(permissionCriteria);
            }
            lc.add(filterCriteria);
            criteria.andOperator(lc);
            Query query = new Query(criteria);
            Query countQuery = Query.of(query);
            query.with(pageable);
            return mongoTemplate.find(query, Template.class).map(this::mapTemplateToTemplateListResponse)
                    .collectList().zipWith(this.countWithFilter(countQuery))
                    .map(p -> new TemplateListFilterResponse(p.getT1(), p.getT2()));
        });
    }


    @Override
    public Mono<Long> countWithFilter(Query query) {
        query.fields().include("id");
        return mongoTemplate.count(query, Template.class);
    }

    private TemplateListResponse mapTemplateToTemplateListResponse(Template template) {
        TemplateListResponse templateDTO = new TemplateListResponse();
        templateDTO.setTemplateId(template.getTemplateId());
        templateDTO.setName(template.getName());
        templateDTO.setDescription(template.getDescription());
        templateDTO.setContent(template.getContent());
        templateDTO.setType(template.getType());
        templateDTO.setEngine(template.getEngine());
        templateDTO.setCreatedTime(template.getCreatedTime());
        templateDTO.setUpdatedTime(template.getUpdatedTime());
        templateDTO.setCreatedBy(template.getCreatedBy());
        templateDTO.setUpdatedBy(template.getUpdatedBy());
        return templateDTO;
    }
}
