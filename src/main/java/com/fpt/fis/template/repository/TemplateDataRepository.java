package com.fpt.fis.template.repository;

import com.fpt.fis.template.repository.entity.Template;
import com.fpt.fis.template.repository.entity.enums.TemplateType;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TemplateDataRepository {

    Flux<Template> findByTypeAndNameOrDescription(TemplateType type, String nameOrDescription, Pageable pageable);

    Mono<Long> countByAndNameOrDescription(TemplateType type, String nameOrDescription);
}
