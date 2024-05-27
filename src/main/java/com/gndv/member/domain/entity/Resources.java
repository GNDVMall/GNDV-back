package com.gndv.member.domain.entity;

import lombok.*;
import org.apache.ibatis.mapping.FetchType;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Getter @ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Resources implements Serializable {

    private Long resource_id;
    private String resource_name;
    private String http_method;
    private int order_num;
    private String resource_type;

    /*
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "role_resources", joinColumns = {@JoinColumn(name = "resource_id") },
            inverseJoinColumns = { @JoinColumn(name = "role_id") })
    @ToString.Exclude
    */
    private Set<Role> roleSet = new HashSet<>();
}