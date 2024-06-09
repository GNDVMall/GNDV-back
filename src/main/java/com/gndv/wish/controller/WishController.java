package com.gndv.wish.controller;

import com.gndv.wish.domain.dto.WishDTO;
import com.gndv.wish.service.WishService;
import com.gndv.item.service.ItemService; // ItemService 임포트 추가
import com.gndv.item.domain.dto.response.ItemDetailResponse; // ItemDetailResponse 임포트 추가
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v2/wish")
@RequiredArgsConstructor
public class WishController {

    private static final Logger logger = LoggerFactory.getLogger(WishController.class);
    private final WishService wishService;
    private final ItemService itemService; // ItemService 주입 추가

    @PostMapping
    public String toggleWish(@RequestBody WishDTO wishDTO, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (userDetails == null) {
                throw new IllegalArgumentException("User must be authenticated to toggle wishlist");
            }

            String userEmail = userDetails.getUsername();  // get email or username from UserDetails
            logger.info("User email: {}", userEmail);

            Long memberId = wishService.getMemberIdByEmail(userEmail);
            wishDTO.setMemberId(memberId);
            logger.info("After setting memberId: {}", wishDTO);

            return wishService.toggleWish(wishDTO);
        } catch (Exception e) {
            logger.error("Error toggling wish", e);
            throw e; // 예외를 다시 던져서 글로벌 예외 처리기로 보내기
        }
    }

    @DeleteMapping
    public String removeWish(@RequestBody WishDTO wishDTO, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (userDetails == null) {
                throw new IllegalArgumentException("User must be authenticated to remove wishlist");
            }

            String userEmail = userDetails.getUsername();  // get email or username from UserDetails
            logger.info("User email: {}", userEmail);

            Long memberId = wishService.getMemberIdByEmail(userEmail);
            wishDTO.setMemberId(memberId);
            logger.info("After setting memberId: {}", wishDTO);

            wishService.removeWish(wishDTO);
            return "Wish removed successfully";
        } catch (Exception e) {
            logger.error("Error removing wish", e);
            throw e; // 예외를 다시 던져서 글로벌 예외 처리기로 보내기
        }
    }

    @GetMapping("/status")
    public boolean isWishListed(@RequestParam Long itemId, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (userDetails == null) {
                throw new IllegalArgumentException("User must be authenticated to check wishlist status");
            }

            String userEmail = userDetails.getUsername();  // get email or username from UserDetails
            logger.info("User email: {}", userEmail);

            Long memberId = wishService.getMemberIdByEmail(userEmail);
            return wishService.isWishListed(memberId, itemId);
        } catch (Exception e) {
            logger.error("Error checking wishlist status", e);
            throw e; // 예외를 다시 던져서 글로벌 예외 처리기로 보내기
        }
    }

    @GetMapping
    public List<ItemDetailResponse> getWishlist(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (userDetails == null) {
                throw new IllegalArgumentException("User must be authenticated to get wishlist");
            }

            String userEmail = userDetails.getUsername();
            logger.info("User email: {}", userEmail);

            Long memberId = wishService.getMemberIdByEmail(userEmail);
            List<WishDTO> wishlist = wishService.getWishlist(memberId);

            // 각 WishDTO의 itemId를 사용하여 아이템 정보를 가져옴
            return wishlist.stream()
                    .filter(wish -> wish.getItemId() != null) // itemId가 null이 아닌 경우만 처리
                    .map(wish -> {
                        try {
                            return itemService.getItem(wish.getItemId());
                        } catch (Exception e) {
                            logger.error("Error fetching item for wish: {}", wish, e);
                            return null; // 예외 발생 시 null 반환
                        }
                    })
                    .filter(item -> item != null) // null이 아닌 경우만 반환
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error fetching wishlist", e);
            throw e;
        }
    }

}