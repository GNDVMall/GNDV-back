package com.gndv.chat.domain.dto.response;

import com.gndv.constant.ChatUserType;
import lombok.Data;

import java.util.Date;

@Data
public class ChatRoomResponse {
    private Long chatroom_id;
    private Date created_at;
    private Date updated_at;
    private Long product_id;
    private Long item_id;
    private String profile_url;

    // 최근 채팅 정보
    private Long chat_user_id;
    private Long member_id;
    private ChatUserType chat_user_type;
    private String chat_content;
    private String nickname;
}
