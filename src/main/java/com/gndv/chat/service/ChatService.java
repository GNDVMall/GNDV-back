package com.gndv.chat.service;

import com.gndv.chat.domain.dto.request.ChatRoomCheckRequest;
import com.gndv.chat.domain.dto.request.ChatRoomCreateRequest;
import com.gndv.chat.domain.dto.response.ChatRoomResponse;
import com.gndv.chat.domain.entity.ChatRoom;
import com.gndv.chat.domain.entity.ChatRoomDetail;
import com.gndv.chat.mapper.ChatMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final ChatMapper chatMapper;

    @PreAuthorize("#chatRoomCreateRequest.email == authentication.name")
    public void createChatRoom(ChatRoomCreateRequest chatRoomCreateRequest) {
        // 채팅방 만들기
        chatMapper.createChatRoom(chatRoomCreateRequest);
    }

    @PreAuthorize("isAuthenticated()")
    public int deleteUserFromChatroom(Long chatroom_id, String name) {
        int updated = chatMapper.deleteUserFromChatroom(chatroom_id, name);
        return updated;
    }

    @PreAuthorize("isAuthenticated()")
    public List<ChatRoomResponse> getChatRooms(String name) {
        List<ChatRoomResponse> list = chatMapper.findAllbyName(name);
        return list;
    }

    @PreAuthorize("isAuthenticated()")
    public ChatRoomDetail getChatRoom(Long chatroom_id, String name) {
        ChatRoomDetail chatRoomDetailResponse = chatMapper.findByIdWithName(chatroom_id, name);
        return chatRoomDetailResponse;
    }

    public ChatRoom checkIsRoom(ChatRoomCheckRequest request) {
        ChatRoom chatroom_id = chatMapper.getChatRoomId(request);
        return chatroom_id;
    }
}
