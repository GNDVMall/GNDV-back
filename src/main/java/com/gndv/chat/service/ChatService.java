package com.gndv.chat.service;

import com.gndv.chat.domain.dto.request.ChatRoomCheckRequest;
import com.gndv.chat.domain.dto.request.ChatRoomCreateRequest;
import com.gndv.chat.domain.dto.request.ChatRoomMessageRequest;
import com.gndv.chat.domain.dto.response.ChatRoomResponse;
import com.gndv.chat.domain.entity.ChatMessage;
import com.gndv.chat.domain.entity.ChatRoom;
import com.gndv.chat.domain.entity.ChatRoomDetail;
import com.gndv.chat.mapper.ChatMapper;
import com.gndv.product.domain.dto.response.ProductDetailResponse;
import com.gndv.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final ChatMapper chatMapper;
    private final ProductMapper productMapper;

//    @PreAuthorize("#chatRoomCreateRequest.email == authentication.name")
    @Transactional
    public void createChatRoom(ChatRoomCreateRequest request) throws Exception {
        // 해당 프로덕트 정보 가져오기
        Optional<ProductDetailResponse> productDetail = productMapper.findById(request.getProduct_id());
        if(productDetail.isEmpty()) throw new Exception("상품 정보가 존재하지 않습니다.");

        // 상품 정보 설정
        request.setSeller(productDetail.get().getEmail());
        request.setItem_id(productDetail.get().getItem_id());

        if(request.getEmail().equals(request.getSeller())) throw new Exception("자신의 상품으로 채팅방을 만들 수 없습니다.");

        // 채팅방 만들기
        chatMapper.createChatRoom(request);
    }

//    @PreAuthorize("isAuthenticated()")
    public int deleteUserFromChatroom(Long chatroom_id, String name) {
        int updated = chatMapper.deleteUserFromChatroom(chatroom_id, name);
        return updated;
    }

//    @PreAuthorize("isAuthenticated()")
    public List<ChatRoomResponse> getChatRooms(String name) {
        List<ChatRoomResponse> list = chatMapper.findAllbyName(name);
        return list;
    }

//    @PreAuthorize("isAuthenticated()")
    public ChatRoomDetail getChatRoom(Long chatroom_id, String name) {
        ChatRoomDetail chatRoomDetailResponse = chatMapper.findByIdWithName(chatroom_id, name);
        return chatRoomDetailResponse;
    }

//    @PreAuthorize("isAuthenticated()")
    public ChatRoom checkIsRoom(ChatRoomCheckRequest request) {
        ChatRoom chatroom_id = chatMapper.getChatRoomId(request);
        return chatroom_id;
    }

    public List<ChatMessage> getChatMessages(ChatRoomMessageRequest request) {
        List<ChatMessage> list = chatMapper.findAllMessagesByIdAndUpdateIsRead(request);
        return list;
    }
}
