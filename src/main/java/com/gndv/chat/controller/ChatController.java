package com.gndv.chat.controller;

import com.gndv.chat.domain.dto.request.ChatRoomCheckRequest;
import com.gndv.chat.domain.dto.request.ChatRoomCreateRequest;
import com.gndv.chat.domain.dto.response.ChatRoomDetailResponse;
import com.gndv.chat.domain.dto.response.ChatRoomListResponse;
import com.gndv.chat.domain.dto.response.ChatRoomResponse;
import com.gndv.chat.domain.entity.ChatRoom;
import com.gndv.chat.domain.entity.ChatRoomDetail;
import com.gndv.chat.service.ChatService;
import com.gndv.common.CustomResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v2/chat")
public class ChatController {
    private final ChatService chatService;
    private final ModelMapper modelMapper;
    
    @GetMapping("/check")
    public CustomResponse<Object> checkIsRoom(Long product_id){
        log.info("이미 생성된 채팅방이 있는지 확인");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        ChatRoomCheckRequest cr = ChatRoomCheckRequest.builder()
                .buyer_email(auth.getName()).product_id(product_id).build();
        log.info("cr?", cr.getBuyer_email(), cr.getProduct_id());

        ChatRoom chatroom = chatService.checkIsRoom(cr);
        if(chatroom == null) return CustomResponse.ok("채팅방이 존재하지 않습니다.", null);
        return CustomResponse.ok("채팅방이 이미 존재합니다.", chatroom.getChatroom_id());
    }

    @GetMapping("")
    public CustomResponse<ChatRoomListResponse> getChatRooms(){
        log.info("Get ChatRoom list");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<ChatRoomResponse> chatrooms = chatService.getChatRooms(auth.getName());

        return CustomResponse.ok("Get ChatRoom list",
                ChatRoomListResponse.builder()
                        .total(chatrooms.size())
                        .chatRoomResponses(chatrooms).build());
    }

    @GetMapping("/{chatrooom_id}")
    public CustomResponse<ChatRoomDetailResponse> getChatRoom(@PathVariable Long chatrooom_id){
        log.info("Get a ChatRoom");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ChatRoomDetail chatroom = chatService.getChatRoom(chatrooom_id, auth.getName());

        ChatRoomDetailResponse chatRoomDetailResponse = modelMapper.map(chatroom, ChatRoomDetailResponse.class);
        chatRoomDetailResponse.setImages(chatroom.getImages());

        return CustomResponse.ok("Get ChatRoom", chatRoomDetailResponse);
    }

    @PostMapping("")
    public CustomResponse<ChatRoomCreateRequest> createRoom(@RequestBody ChatRoomCreateRequest chatRoomCreateRequest) {
        log.info("Create ChatRoom {} ", chatRoomCreateRequest);
        // 채팅방을 만들 때, 채팅방 + 채팅방 유저도 만들어야 한다.
        chatService.createChatRoom(chatRoomCreateRequest);
        // 채팅방 번호를 바로 돌려줘야, 생성 후 바로 이동 가능
        return CustomResponse.ok("Create new ChatRoom", chatRoomCreateRequest);
    }


    @DeleteMapping("/{chatroom_id}")
    public CustomResponse deleteUserFromChatroom(@PathVariable Long chatroom_id) throws Exception {
        log.info("Leave ChatRoom {}", chatroom_id);
        // 현재 로그인한 사용자가 채팅방을 떠나야함
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        int updated = chatService.deleteUserFromChatroom(chatroom_id, auth.getName());
        if(updated != 1){
            // 나중에 예외처리 강의 후 수정
            throw new Exception("채팅방 떠나기 실패");
        }
        return CustomResponse.ok("Leave ChatRoom ok");
    }
}
