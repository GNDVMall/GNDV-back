package com.gndv.chat.domain.entity;

import com.gndv.constant.Boolean;
import lombok.Getter;

import java.util.Date;

@Getter
public class ChatMessage {
    private Long message_id;
    private String chat_content;
    private Date sent_at;
    private Long member_id;
    private Boolean is_read;
    private Date read_at;
    private Long chatroom_id;
}
