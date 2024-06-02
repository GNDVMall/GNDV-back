package com.gndv.chat.domain.entity;

import lombok.Getter;

import java.util.Date;

@Getter
public class ChatRoom {
    private Long chatroom_id;
    private Date created_at;
    private Date updated_at;
    private Long product_id;
    private Long item_id;
}
