package com.gndv.image.domain.entity;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Image {
    private Long image_id;
    private String image_type;
    private Long use_id;
    private String original_name;
    private String real_filename;
    private String content_type;
    private Long size;
    private LocalDateTime created_at;
}
