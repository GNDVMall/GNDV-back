package com.gndv.chat.domain.dto.response;

import com.gndv.constant.ChatMessageUserType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatMessageResponse {
    private String content;
    private String email;
    private Long message_id;
    private ChatMessageUserType message_user_type;
}
