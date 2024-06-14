package com.gndv.search.service;

import com.gndv.search.domain.entity.Search;
import com.gndv.search.domain.entity.Theme;
import com.gndv.search.domain.request.SearchItemRequest;
import com.gndv.search.mapper.SearchMapper;
import com.gndv.search.mapper.ThemeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchMapper searchMapper;
    private final ThemeMapper themeMapper;

    @Transactional
    public void saveSearchKeyword(String keyword) {
        searchMapper.insertSearchInput(keyword);

        Optional<Search> existingSearch = searchMapper.findSearchByKeyword(keyword);
        if (existingSearch.isPresent()) {
            searchMapper.updateSearchCount(keyword);
        } else {
            searchMapper.insertSearch(keyword);
        }
    }

    public List<String> getRecentSearches() {
        return searchMapper.findRecentSearches();
    }

    public List<String> getPopularSearches() {
        return searchMapper.findPopularSearches();
    }

    public List<SearchItemRequest> searchItems(String keyword, String sortBy, String sortOrder, Long minPrice, Long maxPrice, String ageRange, List<Long> themeIds) {
        return searchMapper.findItemsByKeyword(keyword, sortBy, sortOrder, minPrice, maxPrice, ageRange, themeIds);
    }

    public List<Theme> getAllThemes() {
        return themeMapper.findAllThemes();
    }
}
