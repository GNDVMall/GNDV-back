package com.gndv.wish.controller;

import com.gndv.wish.domain.dto.WishDTO;
import com.gndv.wish.service.WishService;
import com.gndv.item.service.ItemService;
import com.gndv.item.domain.dto.response.ItemDetailResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@Tag(name = "Wish API", description = "위시리스트 관련 API")
public class WishController {

    private static final Logger logger = LoggerFactory.getLogger(WishController.class);
    private final WishService wishService;
    private final ItemService itemService;

    @Operation(summary = "위시리스트 토글", description = "위시리스트에 아이템을 추가하거나 제거합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "위시리스트 토글 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping
    public String toggleWish(
            @Parameter(description = "위시리스트 DTO", required = true) @RequestBody WishDTO wishDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
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

    @Operation(summary = "위시리스트 제거", description = "위시리스트에서 아이템을 제거합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "위시리스트 제거 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @DeleteMapping
    public String removeWish(
            @Parameter(description = "위시리스트 DTO", required = true) @RequestBody WishDTO wishDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
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

    @Operation(summary = "위시리스트 상태 확인", description = "특정 아이템이 위시리스트에 있는지 확인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "위시리스트 상태 확인 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/status")
    public boolean isWishListed(
            @Parameter(description = "아이템 ID", required = true) @RequestParam Long itemId,
            @AuthenticationPrincipal UserDetails userDetails) {
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

    @Operation(summary = "위시리스트 조회", description = "사용자의 위시리스트를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "위시리스트 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
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
