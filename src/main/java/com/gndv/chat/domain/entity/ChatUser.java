package com.gndv.chat.domain.entity;

import com.gndv.constant.ChatUserType;
import lombok.Getter;

@Getter
public class ChatUser {
    private Long chat_user_id;
    private Long member_id;
    private ChatUserType chat_user_type;
    private Long chatroom_id;
}
