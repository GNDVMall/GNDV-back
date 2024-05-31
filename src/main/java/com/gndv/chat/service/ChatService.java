package com.gndv.chat.service;

import com.gndv.chat.domain.dto.request.ChatRoomCreateRequest;
import com.gndv.chat.domain.dto.response.ChatRoomResponse;
import com.gndv.chat.mapper.ChatMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final ChatMapper chatMapper;
    public void createChatRoom(ChatRoomCreateRequest chatRoomCreateRequest) {
        // 채팅방 만들기
        chatMapper.createChatRoom(chatRoomCreateRequest);
    }

    public int deleteUserFromChatroom(Long chatroom_id, String name) {
        int updated = chatMapper.deleteUserFromChatroom(chatroom_id, name);
        return updated;
    }

    public List<ChatRoomResponse> getChatRooms(String name) {
        List<ChatRoomResponse> list = chatMapper.findAllbyName(name);
        return list;
    }
}
