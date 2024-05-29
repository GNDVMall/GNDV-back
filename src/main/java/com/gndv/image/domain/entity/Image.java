package com.gndv.image.domain.entity;

import lombok.Getter;

import java.util.Date;

@Getter
public class Image {
    private Long image_id;
    private String image_type;
    private Long use_id;
    private String original_name; // 파일이름
    private String real_filename; // 파일명
    private String content_type;
    private Long size;
    private Date created_at;
}
