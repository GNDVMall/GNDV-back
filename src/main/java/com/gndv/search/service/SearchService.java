package com.gndv.search.service;

import com.gndv.item.domain.entity.Item;
import com.gndv.item.mapper.ItemMapper;
import com.gndv.search.domain.entity.Code;
import com.gndv.search.domain.entity.Search;
import com.gndv.search.domain.request.SearchItemRequest;
import com.gndv.search.mapper.CodeMapper;
import com.gndv.search.mapper.SearchMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchMapper searchMapper;
    private final CodeMapper codeMapper;
    private final ItemMapper itemMapper;

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

    public List<Code> getCategoryCodes() {
        return codeMapper.findCodesByType("CATEGORY");
    }

    public List<SearchItemRequest> searchItems(String keyword) {
        return searchMapper.findItemsByKeyword(keyword);
    }
}
