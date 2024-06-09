package com.gndv.chat.controller;

import com.gndv.chat.domain.dto.request.ChatMessageRequest;
import com.gndv.chat.service.ChatSocketService;
import com.gndv.member.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketController {
    private final ChatSocketService chatSocketService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/v2/chat/send/{chatroom_id}")
    public void sendMessage(@DestinationVariable("chatroom_id") Long chatroom_id, @Payload ChatMessageRequest chatMessageRequest, SimpMessageHeaderAccessor simpMessageHeaderAccessor) throws Exception {
        log.info("Send a Message! , {}", chatMessageRequest);

        // chat/send/채팅방으로 온 메시지를 구독한 곳으로 보내준다.
        simpMessagingTemplate.convertAndSend("/topic/" + chatroom_id, chatMessageRequest);

        // 메시지를 데이터 베이스에 저장한다. -> 추후 개선 필요
        Member member = (Member) simpMessageHeaderAccessor.getSessionAttributes().get("member");

        chatMessageRequest.setChatroom_id(chatroom_id);
        chatMessageRequest.setEmail(member.getEmail());

        int updated = chatSocketService.insertMessage(chatMessageRequest);
        if(updated != 1){
            throw new Exception("채팅 메시지 추가 에러");
        }
    }
}
