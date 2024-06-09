package com.gndv.wish.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishDTO {
    private Long wishId;
    private Long memberId;
    private Long itemId;
    private LocalDateTime createdAt;
}
