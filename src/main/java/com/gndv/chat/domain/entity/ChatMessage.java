package com.gndv.chat.domain.entity;

import com.gndv.constant.Boolean;
import com.gndv.constant.ChatMessageType;
import com.gndv.constant.ChatMessageUserType;
import lombok.Getter;

import java.util.Date;

@Getter
public class ChatMessage {
    private Long message_id;
    private String chat_content;
    private Date sent_at;
    private String nickname;
    private Boolean is_read;
    private Date read_at;
    private ChatMessageType message_type;
    private ChatMessageUserType message_user_type;
    private String content_type;
}
