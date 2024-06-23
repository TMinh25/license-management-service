package com.fpt.fis.template.repository;

import com.fpt.fis.template.repository.entity.Template;

import reactor.core.publisher.Mono;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateRepository extends ReactiveMongoRepository<Template, String>, TemplateDataRepository {

    Mono<Boolean> existsByName(String name);

    Mono<Template> findByName(String name);

}
