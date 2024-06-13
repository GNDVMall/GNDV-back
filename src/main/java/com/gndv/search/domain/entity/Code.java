package com.gndv.search.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Code {

    private Long code_id;
    private String code_type;
    private String code_value;
    private String code_key;
}
