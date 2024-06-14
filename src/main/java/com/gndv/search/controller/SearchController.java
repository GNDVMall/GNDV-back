package com.gndv.search.controller;

import com.gndv.common.CustomResponse;
import com.gndv.item.domain.entity.Item;
import com.gndv.search.domain.entity.Theme;
import com.gndv.search.domain.request.SearchItemRequest;
import com.gndv.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public CustomResponse<List<String>> getRecentSearches() {
        List<String> recentSearches = searchService.getRecentSearches();
        return CustomResponse.ok("Recent searches fetched successfully", recentSearches);
    }

    @GetMapping("/popular")
    public CustomResponse<List<String>> getPopularSearches() {
        List<String> popularSearches = searchService.getPopularSearches();
        return CustomResponse.ok("Popular searches fetched successfully", popularSearches);
    }

    @GetMapping
    public CustomResponse<List<SearchItemRequest>> searchItems(
            @RequestParam String keyword,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder,
            @RequestParam(required = false) Long minPrice,
            @RequestParam(required = false) Long maxPrice,
            @RequestParam(required = false) String ageRange,
            @RequestParam(required = false) List<Long> themeIds) {

        List<SearchItemRequest> items = searchService.searchItems(keyword, sortBy, sortOrder, minPrice, maxPrice, ageRange, themeIds);
        return CustomResponse.ok("Items fetched successfully", items);
    }

    @GetMapping("/themes")
    public CustomResponse<List<Theme>> getAllThemes() {
        List<Theme> themes = searchService.getAllThemes();
        return CustomResponse.ok("Themes fetched successfully", themes);
    }
}
