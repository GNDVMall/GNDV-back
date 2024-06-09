package com.gndv.chat.domain.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatMessageResponse {
    private String content;
    private String email;
    private Long message_id;
}
