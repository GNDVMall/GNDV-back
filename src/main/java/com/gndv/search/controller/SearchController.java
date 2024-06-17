package com.gndv.search.controller;

import com.gndv.common.CustomResponse;
import com.gndv.common.domain.request.PagingRequest;
import com.gndv.common.domain.response.PageResponse;
import com.gndv.search.domain.entity.Theme;
import com.gndv.search.domain.request.SearchItemRequest;
import com.gndv.search.service.SearchService;
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
public class SearchController {

    private final SearchService searchService;

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

    @GetMapping("/popular")
    public CustomResponse<List<String>> getPopularSearches() {
        List<String> popularSearches = searchService.getPopularSearches();
        return CustomResponse.ok("Popular searches fetched successfully", popularSearches);
    }

    @GetMapping
    public CustomResponse<PageResponse<SearchItemRequest>> searchItems(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam String keyword,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder,
            @RequestParam(required = false) Long minPrice,
            @RequestParam(required = false) Long maxPrice,
            @RequestParam(required = false) String ageRange,
            @RequestParam(required = false) List<Long> themeIds,
            @RequestParam(required = false, defaultValue = "1") int pageNo,
            @RequestParam(required = false, defaultValue = "10") int size) {

        PagingRequest pagingRequest = new PagingRequest(pageNo, size);
        Long member_id = null;

        if (user != null) {
            String email = user.getUsername();
            member_id = searchService.getMemberIdByEmail(email);
        }

        PageResponse<SearchItemRequest> items = searchService.searchItems(member_id, keyword, sortBy, sortOrder, minPrice, maxPrice, ageRange, themeIds, pagingRequest);
        return CustomResponse.ok("Items fetched successfully", items);
    }

    @GetMapping("/themes")
    public CustomResponse<List<Theme>> getAllThemes() {
        List<Theme> themes = searchService.getAllThemes();
        return CustomResponse.ok("Themes fetched successfully", themes);
    }
}
