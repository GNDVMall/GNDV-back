package com.gndv.search.service;

import com.gndv.item.domain.entity.Item;
import com.gndv.item.mapper.ItemMapper;
import com.gndv.search.mapper.SearchMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {

    private final ItemMapper itemMapper;
    private final SearchMapper searchMapper;

    @Transactional
    public List<Item> searchItems(String keyword, Double minPrice, Double maxPrice, Integer ageRange, Integer pieces) {
        searchMapper.insertOrUpdateSearch(keyword);
        return itemMapper.findItems(keyword, minPrice, maxPrice, ageRange, pieces);
    }

    public List<String> getRecentSearches() {
        return searchMapper.findRecentSearches();
    }

    public List<String> getPopularSearches() {
        return searchMapper.findPopularSearches();
    }
}
