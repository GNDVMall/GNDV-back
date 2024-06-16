package com.gndv.item.domain.entity;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Item {
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
    private Long recommend;
    private String image_url;
}
