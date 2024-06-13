package com.gndv.search.controller;

import com.gndv.item.domain.entity.Item;
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
    public ResponseEntity<List<String>> getRecentSearches() {
        List<String> recentSearches = searchService.getRecentSearches();
        return ResponseEntity.ok(recentSearches);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<String>> getPopularSearches() {
        List<String> popularSearches = searchService.getPopularSearches();
        return ResponseEntity.ok(popularSearches);
    }

    @GetMapping
    public ResponseEntity<List<SearchItemRequest>> searchItems(@RequestParam String keyword) {
        searchService.saveSearchKeyword(keyword);
        List<SearchItemRequest> items = searchService.searchItems(keyword);
        return ResponseEntity.ok(items);
    }
}
