package com.fpt.fis.template.repository.impl;

import com.fpt.fis.template.model.response.TemplateListFilterResponse;
import com.fpt.fis.template.model.response.TemplateListResponse;
import com.fpt.fis.template.repository.TemplateDataRepository;
import com.fpt.fis.template.repository.entity.Template;
import com.fpt.framework.security.support.data.nonrelation.query.ReactiveNonRelationPermissionQuery;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Mono;

public class TemplateDataRepositoryImpl implements TemplateDataRepository {

    private final ReactiveMongoTemplate mongoTemplate;
    private final ReactiveNonRelationPermissionQuery permissionQuery;

    public TemplateDataRepositoryImpl(ApplicationContext applicationContext, ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        this.permissionQuery = applicationContext.getBean(ReactiveNonRelationPermissionQuery.class);
    }

    @Override
    public Mono<TemplateListFilterResponse> findTemplatesWithFilter(String searchText, Pageable pageable) {
        return permissionQuery.buildPermissionCriteria().flatMap(pc -> {
            Criteria criteria = new Criteria();
            if (searchText != null && !searchText.isEmpty()) {
                criteria = criteria.orOperator(Criteria.where("name").regex(searchText, "i"), Criteria.where("description").regex(searchText, "i"));
            }
            Query query = new Query(criteria);
            Query countQuery = Query.of(query);
            query.with(pageable);
            return mongoTemplate.find(query, Template.class).map(this::mapTemplateToTemplateListResponse)
                    .collectList().zipWith(this.countTemplatesWithFilter(countQuery))
                    .map(p -> new TemplateListFilterResponse(p.getT1(), p.getT2()));
        });
    }


    @Override
    public Mono<Long> countTemplatesWithFilter(Query query) {
        query.fields().include("id");
        return mongoTemplate.count(query, Template.class);
    }

    private TemplateListResponse mapTemplateToTemplateListResponse(Template template) {
        TemplateListResponse templateDTO = new TemplateListResponse();
        templateDTO.setTemplateId(template.getId());
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
