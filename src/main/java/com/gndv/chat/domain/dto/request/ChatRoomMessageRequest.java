package com.gndv.chat.domain.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatRoomMessageRequest {
    private Long chatroom_id;
    private String email;
}
