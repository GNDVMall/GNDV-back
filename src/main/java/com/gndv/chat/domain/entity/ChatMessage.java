package com.gndv.chat.domain.entity;

import com.gndv.constant.Boolean;
import lombok.Getter;

import java.util.Date;

@Getter
public class ChatMessage {
    private Long message_id;
    private Long member_id;
    private Long chatroom_id;
    private String chat_content;
    private String email;
    private Boolean is_read;
    private Date sent_at;
    private Date read_at;
}
