package com.fpt.fis.template.repository.impl;

import com.fpt.fis.template.repository.TemplateDataRepository;
import com.fpt.fis.template.repository.entity.Template;
import com.fpt.fis.template.repository.entity.enums.TemplateType;
import com.fpt.framework.security.support.data.nonrelation.query.ReactiveNonRelationPermissionQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class TemplateDataRepositoryImpl implements TemplateDataRepository {

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    @Autowired
    private ReactiveNonRelationPermissionQuery permissionQuery;

    @Override
    public Flux<Template> findByTypeAndNameOrDescription(TemplateType type, String nameOrDescription, Pageable pageable) {
        Query query = buildQuery(type, nameOrDescription).with(pageable);
        return this.permissionQuery.appendQueryPermission(query).flatMapMany(q -> mongoTemplate.find(q, Template.class));
    }

    private Query buildQuery(TemplateType type, String nameOrDescription) {
        Query query = new Query();
        if (type != null) {
            query.addCriteria(Criteria.where("type").is(type));
        }
        if (StringUtils.isNotBlank(nameOrDescription)) {
            query.addCriteria(new Criteria().orOperator(Criteria.where("name").regex(nameOrDescription, "i"), Criteria.where("description").regex(nameOrDescription, "i")));
        }
        return query;
    }

    @Override
    public Mono<Long> countByAndNameOrDescription(TemplateType type, String nameOrDescription) {
        Query query = buildQuery(type, nameOrDescription);
        return this.permissionQuery.appendQueryPermission(query).flatMap(q -> mongoTemplate.count(q, Template.class));
    }
}
