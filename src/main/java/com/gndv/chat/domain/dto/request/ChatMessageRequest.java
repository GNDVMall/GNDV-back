package com.gndv.chat.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatMessageRequest {
    private String content; // 내용
    private String email; // 보낸 사람
    private String receiver; // 받는 사람
    private Long chatroom_id;
    private Long message_id;
}