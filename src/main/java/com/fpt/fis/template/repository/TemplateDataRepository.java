package com.fpt.fis.template.repository;

import com.fpt.fis.template.model.response.TemplateListFilterResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Mono;

public interface TemplateDataRepository {
    Mono<TemplateListFilterResponse> findTemplatesWithFilter(String searchText, Pageable pageable);

    Mono<Long> countTemplatesWithFilter(Query query);

}
