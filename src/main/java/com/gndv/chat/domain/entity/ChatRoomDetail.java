package com.gndv.chat.domain.entity;

import com.gndv.constant.ChatUserType;
import lombok.Getter;

import java.util.Date;

@Getter
public class ChatRoomDetail {
    private Long chatroom_id;
    private Long product_id;
    private Date created_at;
    private Date updated_at;

    // 상대방 유저 정보
    private String nickname;
    private Long rating;
    private String email;
    private ChatUserType chat_user_type;
    private String images;
}
