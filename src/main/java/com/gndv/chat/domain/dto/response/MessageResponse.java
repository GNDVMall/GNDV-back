package com.gndv.chat.domain.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageResponse {
    private String content;
    private String email;
}
