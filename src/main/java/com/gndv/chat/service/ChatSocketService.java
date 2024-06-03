package com.gndv.chat.service;

import com.gndv.chat.domain.dto.request.ChatMessageRequest;
import com.gndv.chat.mapper.ChatSocketMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatSocketService {
    private final ChatSocketMapper chatSocketMapper;

    public int insertMessage(ChatMessageRequest chatMessageRequest) {
        System.out.println("============서비스===========");
        System.out.println(chatMessageRequest);
        int updated = chatSocketMapper.insertMessage(chatMessageRequest);
        return updated;
    }
}
