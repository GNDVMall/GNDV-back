package com.gndv.member.domain.entity;

import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleHierarchy implements Serializable {

    private Long role_hierarchy_id;
    private String role_name;

    /*
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "id", insertable = false, updatable = false)
    */
    private RoleHierarchy parent;

    /*
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    */
    private Set<RoleHierarchy> children = new HashSet<>();
}
