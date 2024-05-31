package com.gndv.chat.controller;

import com.gndv.chat.domain.dto.request.ChatRoomCreateRequest;
import com.gndv.chat.domain.dto.response.ChatRoomListResponse;
import com.gndv.chat.domain.dto.response.ChatRoomResponse;
import com.gndv.chat.service.ChatService;
import com.gndv.common.CustomResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatService chatService;

    @GetMapping("")
    @PreAuthorize("isAuthenticated()")
    public CustomResponse<ChatRoomListResponse> getChatRooms(){
        log.info("Get ChatRoom list");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<ChatRoomResponse> chatrooms = chatService.getChatRooms(auth.getName());

        return CustomResponse.ok("Get ChatRoom list",
                ChatRoomListResponse.builder()
                        .total(chatrooms.size())
                        .chatRoomResponses(chatrooms).build());
    }

    @PostMapping("")
    @PreAuthorize("#chatRoomCreateRequest.email == authentication.name")
    public CustomResponse<ChatRoomCreateRequest> createRoom(@RequestBody ChatRoomCreateRequest chatRoomCreateRequest) {
        log.info("Create ChatRoom {} ", chatRoomCreateRequest);
        // 채팅방을 만들 때, 채팅방 + 채팅방 유저도 만들어야 한다.
        chatService.createChatRoom(chatRoomCreateRequest);
        // 채팅방 번호를 바로 돌려줘야, 생성 후 바로 이동 가능
        return CustomResponse.ok("Create new ChatRoom", chatRoomCreateRequest);
    }


    @DeleteMapping("/{chatroom_id}")
    @PreAuthorize("isAuthenticated()")
    public CustomResponse deleteUserFromChatroom(@PathVariable Long chatroom_id) throws Exception {
        log.info("Leave ChatRoom {}", chatroom_id);
        // 현재 로그인한 사용자가 채팅방을 떠나야함
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        int updated = chatService.deleteUserFromChatroom(chatroom_id, auth.getName());
        log.info("update => {}", updated);
        if(updated != 1){
            // 나중에 예외처리 강의 후 수정
            throw new Exception("채팅방 떠나기 실패");
        }
        return CustomResponse.ok("Leave ChatRoom ok");
    }
}
