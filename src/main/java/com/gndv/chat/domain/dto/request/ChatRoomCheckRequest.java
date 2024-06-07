package com.gndv.chat.domain.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatRoomCheckRequest {
    private Long product_id;
    private String buyer_email;
}
