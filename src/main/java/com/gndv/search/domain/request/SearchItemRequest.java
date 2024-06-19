package com.gndv.search.domain.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SearchItemRequest {

    private Long item_id;
    private String item_number;
    private Long regular_price;
    private String age_range;
    private Long pieces;
    private String item_name;
    private Long recent_price;
    private LocalDateTime release_date;
    private Long wish_count;
    private Long view_count;
    private Long theme_id;
    private String theme_name;
    private Long recommend;
    private String image_url;
}
