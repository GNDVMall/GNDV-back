package com.gndv.chat.domain.dto.request;


import lombok.Data;

@Data
public class ChatRoomCreateRequest {
    private String email; // 검증용
    private Long member_id;
    private Long seller_id;
    private Long product_id;
    private Long item_id;
    private Long chatRoomId;
}


