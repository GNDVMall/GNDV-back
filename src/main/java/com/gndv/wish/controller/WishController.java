package com.gndv.wish.controller;

import com.gndv.wish.domain.dto.WishDTO;
import com.gndv.wish.service.WishService;
import com.gndv.item.service.ItemService;
import com.gndv.item.domain.dto.response.ItemDetailResponse;
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
    private final ItemService itemService;

    @PostMapping
    public String toggleWish(@RequestBody WishDTO wishDTO, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            throw new IllegalArgumentException("User must be authenticated to toggle wishlist");
        }

        try {
            String userEmail = userDetails.getUsername();
            logger.info("User email: {}", userEmail);

            Long memberId = wishService.getMemberIdByEmail(userEmail);
            wishDTO.setMemberId(memberId);
            logger.info("After setting memberId: {}", wishDTO);

            return wishService.toggleWish(wishDTO);
        } catch (Exception e) {
            logger.error("Error toggling wish", e);
            throw e;
        }
    }

    @DeleteMapping
    public String removeWish(@RequestBody WishDTO wishDTO, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            throw new IllegalArgumentException("User must be authenticated to remove wishlist");
        }

        try {
            String userEmail = userDetails.getUsername();
            logger.info("User email: {}", userEmail);

            Long memberId = wishService.getMemberIdByEmail(userEmail);
            wishDTO.setMemberId(memberId);
            logger.info("After setting memberId: {}", wishDTO);

            wishService.removeWish(wishDTO);
            return "Wish removed successfully";
        } catch (Exception e) {
            logger.error("Error removing wish", e);
            throw e;
        }
    }

    @GetMapping("/status")
    public boolean isWishListed(@RequestParam Long itemId, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            throw new IllegalArgumentException("User must be authenticated to check wishlist status");
        }

        try {
            String userEmail = userDetails.getUsername();
            logger.info("User email: {}", userEmail);

            Long memberId = wishService.getMemberIdByEmail(userEmail);
            return wishService.isWishListed(memberId, itemId);
        } catch (Exception e) {
            logger.error("Error checking wishlist status", e);
            throw e;
        }
    }

    @GetMapping
    public List<ItemDetailResponse> getWishlist(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            throw new IllegalArgumentException("User must be authenticated to get wishlist");
        }

        try {
            String userEmail = userDetails.getUsername();
            logger.info("User email: {}", userEmail);

            Long memberId = wishService.getMemberIdByEmail(userEmail);

            List<WishDTO> wishlist = wishService.getWishlist(memberId);

            // 각 WishDTO의 itemId를 사용하여 아이템 정보를 가져옴
            return wishlist.stream()
                    .filter(wish -> wish.getItemId() != null)
                    .map(wish -> {
                        try {
                            return itemService.getItem(wish.getItemId());
                        } catch (Exception e) {
                            logger.error("Error fetching item for wish: {}", wish, e);
                            return null;
                        }
                    })
                    .filter(item -> item != null)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error fetching wishlist", e);
            throw e;
        }
    }
}
