package com.fpt.fis.template.repository;

import com.fpt.fis.template.model.FilterDTO;
import com.fpt.fis.template.model.response.TemplateListFilterResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Mono;

public interface TemplateDataRepository {
    Mono<TemplateListFilterResponse> findWithFilter(FilterDTO filter, Pageable pageable);

    Mono<Long> countWithFilter(Query query);

}
