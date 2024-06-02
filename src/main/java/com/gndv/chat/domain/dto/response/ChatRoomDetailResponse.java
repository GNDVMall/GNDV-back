package com.gndv.chat.domain.dto.response;

import com.gndv.constant.ChatUserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDetailResponse {
    private Long chatroom_id;
    private Long product_id;
    private Date created_at;
    private Date updated_at;
    
    // 상대방 유저 정보
    private String nickname;
    private Long rating;
    private String email;
    private ChatUserType chat_user_type;
    private String[] images;

    public void setImages(String images) {
        this.images = images != null ? images.split(",") : new String[0];
    }
}