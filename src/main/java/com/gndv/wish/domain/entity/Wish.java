package com.gndv.wish.domain.entity;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wish {
    private Long wishId;
    private Long memberId;
    private Long itemId;
    private LocalDateTime createdAt;
}
