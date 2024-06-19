package com.gndv.chat.domain.dto.response;

import com.gndv.chat.domain.entity.ChatMessage;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ChatRoomMessageResponse{
    private List<ChatMessage> list;
}
