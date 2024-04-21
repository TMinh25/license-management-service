package com.fpt.fis.template.model;

import com.fpt.fis.template.model.enums.FilterConjunctionType;
import com.fpt.fis.template.model.enums.FilterDataType;
import com.fpt.fis.template.model.enums.FilterOperatorEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FilterDTO {

    private String field;

    private FilterOperatorEnum operator;

    private FilterDataType dataType;

    private Object value;

    private List<FilterDTO> subFilters;

    private String filterString;

    private FilterConjunctionType conjunction;

}
