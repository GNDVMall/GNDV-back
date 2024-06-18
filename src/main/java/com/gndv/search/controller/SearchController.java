package com.gndv.search.controller;

import com.gndv.common.CustomResponse;
import com.gndv.common.domain.request.PagingRequest;
import com.gndv.common.domain.response.PageResponse;
import com.gndv.search.domain.entity.Theme;
import com.gndv.search.domain.request.SearchItemRequest;
import com.gndv.search.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v2/search")
@RequiredArgsConstructor
@Tag(name = "Search API", description = "검색 관련 API")
public class SearchController {

    private final SearchService searchService;

    @Operation(summary = "최근 검색어 조회", description = "사용자의 최근 검색어를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "최근 검색어 조회 성공", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/recent")
    public CustomResponse<List<String>> getRecentSearches(@AuthenticationPrincipal UserDetails user) {
        List<String> recentSearches;

        if (user == null) {
            recentSearches = List.of();
        } else {
            String email = user.getUsername();
            Long member_id = searchService.getMemberIdByEmail(email);
            recentSearches = searchService.getRecentSearchesByMemberId(member_id);
        }

        return CustomResponse.ok("Recent searches fetched successfully", recentSearches);
    }

    @Operation(summary = "인기 검색어 조회", description = "인기 검색어를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "인기 검색어 조회 성공", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/popular")
    public CustomResponse<List<String>> getPopularSearches() {
        List<String> popularSearches = searchService.getPopularSearches();
        return CustomResponse.ok("Popular searches fetched successfully", popularSearches);
    }

    @Operation(summary = "아이템 검색", description = "키워드를 사용하여 아이템을 검색합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "아이템 검색 성공", content = @Content(schema = @Schema(implementation = PageResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping
    public CustomResponse<PageResponse<SearchItemRequest>> searchItems(
            @AuthenticationPrincipal UserDetails user,
            @Parameter(description = "검색 키워드", required = true) @RequestParam String keyword,
            @Parameter(description = "정렬 기준") @RequestParam(required = false) String sortBy,
            @Parameter(description = "정렬 순서") @RequestParam(required = false) String sortOrder,
            @Parameter(description = "최소 가격") @RequestParam(required = false) Long minPrice,
            @Parameter(description = "최대 가격") @RequestParam(required = false) Long maxPrice,
            @Parameter(description = "연령대") @RequestParam(required = false) String ageRange,
            @Parameter(description = "테마 ID 목록") @RequestParam(required = false) List<Long> themeIds,
            @Parameter(description = "페이지 번호", required = false, schema = @Schema(defaultValue = "1")) @RequestParam(required = false, defaultValue = "1") int pageNo,
            @Parameter(description = "페이지 크기", required = false, schema = @Schema(defaultValue = "10")) @RequestParam(required = false, defaultValue = "10") int size) {

        PagingRequest pagingRequest = new PagingRequest(pageNo, size);
        Long member_id = null;

        if (user != null) {
            String email = user.getUsername();
            member_id = searchService.getMemberIdByEmail(email);
        }

        PageResponse<SearchItemRequest> items = searchService.searchItems(member_id, keyword, sortBy, sortOrder, minPrice, maxPrice, ageRange, themeIds, pagingRequest);
        return CustomResponse.ok("Items fetched successfully", items);
    }

    @Operation(summary = "모든 테마 조회", description = "모든 테마를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "테마 조회 성공", content = @Content(schema = @Schema(implementation = Theme.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/themes")
    public CustomResponse<List<Theme>> getAllThemes() {
        List<Theme> themes = searchService.getAllThemes();
        return CustomResponse.ok("Themes fetched successfully", themes);
    }
}
