package com.gndv.chat.domain.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ChatRoomListResponse {
    private List<ChatRoomResponse> chatRoomResponses;
    private Integer total;
}
