package com.gndv.chat.controller;

import com.gndv.chat.domain.dto.request.ChatMessageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketController {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/v2/chat/send/{chatroom_id}")
//    @PreAuthorize("#chatMessageRequest.email == authentication.name")
    public void sendMessage(@DestinationVariable("chatroom_id") Long chatroom_id, @Payload ChatMessageRequest chatMessageRequest){
        log.info("Send a Message! , {}", chatMessageRequest);
        // chat/send/채팅방으로 온 메시지를 구독한 곳으로 보내준다.
        simpMessagingTemplate.convertAndSend("/topic/" + chatroom_id, chatMessageRequest);
    }
}
