package com.gndv.chat.controller;

import com.gndv.chat.domain.dto.request.MessageRequest;
import com.gndv.chat.domain.dto.response.MessageResponse;
import com.gndv.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatWebsocketController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/chat/send/{room_id}")
    public void sendMsg(@DestinationVariable("room_id") String roomId, @Payload MessageRequest message){
        log.info("소켓 메세지 전달");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // 데이터베이스에 저장

        MessageResponse data = MessageResponse.builder()
                .content(message.getContent())
                .email(auth.getName())
                .build();
        simpMessagingTemplate.convertAndSend("/topic/" + roomId ,data);
    }
}
