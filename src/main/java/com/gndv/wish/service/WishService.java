package com.gndv.wish.service;

import com.gndv.wish.domain.dto.WishDTO;

import java.util.List;

public interface WishService {
    Long getMemberIdByEmail(String email);
    String toggleWish(WishDTO wishDTO);
    boolean isWishListed(Long memberId, Long itemId);
    List<WishDTO> getWishlist(Long memberId);
    void removeWish(WishDTO wishDTO); // 새 메서드 추가
}
