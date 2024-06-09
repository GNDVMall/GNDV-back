package com.gndv.chat.domain.entity;

import com.gndv.constant.ChatUserType;
import com.gndv.constant.ProductStatus;
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

    // 프로덕트 정보
    private String profile_url;
    private String price;
    private String title;
    private ProductStatus product_status;
}
