package com.gndv.chat.controller;

import com.gndv.chat.domain.dto.request.ChatRoomCreateRequest;
import com.gndv.chat.service.ChatService;
import com.gndv.common.CustomResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatService chatService;

    @PostMapping("")
    @PreAuthorize("#chatRoomCreateRequest.email == authentication.name")
    public CustomResponse<Object> createRoom(@RequestBody ChatRoomCreateRequest chatRoomCreateRequest){
        log.info("Create ChatRoom {} ", chatRoomCreateRequest);
        // 채팅방을 만들 때, 채팅방 + 채팅방 유저도 만들어야 한다.
        chatService.createChatRoom(chatRoomCreateRequest);
        // 채팅방 번호를 바로 돌려줘야, 생성 후 바로 이동 가능
        return CustomResponse.ok("Create new ChatRoom", chatRoomCreateRequest);
    }
}
