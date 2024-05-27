package com.gndv.member.domain.dto;

import com.gndv.member.domain.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourcesDTO {

    private String resource_id;
    private String resource_name;
    private String http_method;
    private int order_num;
    private String resource_type;
    private String role_name;
    private Set<Role> roleSet;
}
