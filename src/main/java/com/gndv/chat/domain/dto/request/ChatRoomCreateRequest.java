package com.gndv.chat.domain.dto.request;


import lombok.Data;

@Data
public class ChatRoomCreateRequest {
    private String email;
    private String seller;
    private Long product_id;
    private Long item_id;
    private Long chatRoomId;
}


