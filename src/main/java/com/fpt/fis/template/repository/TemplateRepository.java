package com.fpt.fis.template.repository;

import com.fpt.fis.template.repository.entity.Template;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TemplateRepository extends ReactiveMongoRepository<Template, String>{

    Flux<Template> findByNameContainingOrDescriptionContaining(String name, String description, Pageable pageable);

    Flux<Template> findByIdNotNull(Pageable pageable);

    Mono<Long> countByNameContainingOrDescriptionContaining(String name, String description);

    Mono<Long> countByIdNotNull();
}
