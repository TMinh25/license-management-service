package com.fpt.fis.template.repository;

import com.fpt.fis.template.repository.entity.Template;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateRepository extends ReactiveMongoRepository<Template, String>, TemplateDataRepository {
}
