package com.gndv.member.domain.entity;

import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role implements Serializable {

    private Long role_id;
    private String role_name;
    private String role_desc;
    private String is_expression;

    /*
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roleSet", cascade = CascadeType.ALL)
    @OrderBy("orderNum desc")
    */
    private Set<Resources> resourcesSet = new LinkedHashSet<>();

    /*
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "userRoles", cascade = CascadeType.ALL)
    */
    private Set<Member> members = new HashSet<>();
}