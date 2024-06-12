package com.gndv.search.domain.entity;

import lombok.Data;

@Data
public class Search {

    private Long search_id;
    private String keyword;
    private Long search_count;
}
