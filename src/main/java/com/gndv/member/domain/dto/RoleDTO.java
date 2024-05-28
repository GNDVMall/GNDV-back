package com.gndv.member.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {

    private String role_id;
    private String role_name;
    private String role_desc;
    private String is_expression;
}
