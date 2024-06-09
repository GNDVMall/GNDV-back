package com.gndv.chat.domain.dto.request;

import lombok.Data;

@Data
public class ChatMessageRequest {
    private String content; // 내용
    private String email; // 보낸 사람
    private Long chatroom_id;
    private Long message_id;
}
