package com.gndv.product.domain.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductInsertWithMemberRequest {
    private Long member_id;
    private String title;
    private String description;
    private String price;
    private String imageUrl;  // 추가된 필드

    // 추가된 getter 메서드
    public String getImageUrl() {
        return imageUrl;
    }
}
