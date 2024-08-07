package com.gndv.chat.controller;

import com.gndv.chat.domain.dto.request.ChatMessageRequest;
import com.gndv.chat.domain.dto.response.ChatAlarmResponse;
import com.gndv.chat.domain.dto.response.ChatMessageResponse;
import com.gndv.chat.service.ChatSocketService;
import com.gndv.member.domain.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Chat WebSocket API", description = "WebSocket을 통한 채팅 메시지 처리 API")
public class ChatWebSocketController {
    private final ChatSocketService chatSocketService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/v2/chat/send/{chatroom_id}")
    @Operation(summary = "채팅 메시지 전송", description = "WebSocket을 통해 특정 채팅방으로 메시지를 전송합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "메시지 전송 성공"),
            @ApiResponse(responseCode = "500", description = "메시지 전송 실패")
    })
    public void sendMessage(
            @Parameter(description = "채팅방 ID", required = true) @DestinationVariable("chatroom_id") Long chatroom_id,
            @Parameter(description = "채팅 메시지 요청", required = true) @Payload ChatMessageRequest chatMessageRequest,
            SimpMessageHeaderAccessor simpMessageHeaderAccessor) throws Exception {
        log.info("Send a Message! , {}", chatMessageRequest);

        // 메시지를 데이터베이스에 저장한다. -> 추후 개선 필요
        Member member = (Member) simpMessageHeaderAccessor.getSessionAttributes().get("member");

        chatMessageRequest.setChatroom_id(chatroom_id);
        chatMessageRequest.setEmail(member.getEmail());

        int updated = chatSocketService.insertMessage(chatMessageRequest);
        log.info("메시지 => {}", chatMessageRequest.getMessage_id());

        ChatMessageResponse response = ChatMessageResponse.builder()
                .message_id(chatMessageRequest.getMessage_id())
                .content(chatMessageRequest.getContent())
                .email(chatMessageRequest.getEmail()) // 나
                .message_user_type(chatMessageRequest.getMessage_user_type()) // USER or SYSTEM
                .content_type(chatMessageRequest.getContent_type())
                .build();

        // chat/send/채팅방으로 온 메시지를 구독한 곳으로 보내준다.
        simpMessagingTemplate.convertAndSend("/topic/" + chatroom_id, response);

        // 해당 메시지를 받는 유저의 이메일로 구독된 곳으로 메시지를 보낸다.
        simpMessagingTemplate.convertAndSend("/topic/" + chatMessageRequest.getReceiver(), new ChatAlarmResponse("리렌더링 요청"));

        if(updated != 1){
            throw new Exception("채팅 메시지 추가 에러");
        }
    }
}
