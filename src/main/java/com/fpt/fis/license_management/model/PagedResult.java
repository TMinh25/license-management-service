package com.fpt.fis.license_management.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class PagedResult<T> {
    private final Page<T> page;

    public PagedResult(Page<T> pagedEntity) {
        this.page = pagedEntity;
    }

    @JsonProperty("records")
    public List<T> getContent() {
        return this.page.getContent();
    }

    @JsonProperty("total")
    public long getTotalElements() {
        return page.getTotalElements();
    }

    @JsonProperty("currentPage")
    public int getNumber() {
        return page.getNumber();
    }

    @JsonProperty("totalPages")
    public int getTotalPages() {
        return page.getTotalPages();
    }
}
