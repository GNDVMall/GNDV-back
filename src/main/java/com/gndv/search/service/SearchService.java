package com.gndv.search.service;

import com.gndv.common.domain.request.PagingRequest;
import com.gndv.common.domain.response.PageResponse;
import com.gndv.member.mapper.MemberMapper;
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
    private final MemberMapper memberMapper;

    @Transactional
    public void saveSearchKeyword(Long member_id, String keyword) {
        searchMapper.insertSearchInput(member_id, keyword);

        Optional<Search> existingSearch = searchMapper.findSearchByKeyword(keyword);
        if (existingSearch.isPresent()) {
            searchMapper.updateSearchCount(keyword);
        } else {
            searchMapper.insertSearch(keyword);
        }
    }

    public List<String> getRecentSearchesByMemberId(Long member_id) {
        return searchMapper.findRecentSearchesByMemberId(member_id);
    }

    public List<String> getPopularSearches() {
        return searchMapper.findPopularSearches();
    }

    public PageResponse<SearchItemRequest> searchItems(String keyword, String sortBy, String sortOrder, Long minPrice, Long maxPrice, String ageRange, List<Long> themeIds, PagingRequest pagingRequest) {
        List<SearchItemRequest> items = searchMapper.findItemsByKeyword(keyword, sortBy, sortOrder, minPrice, maxPrice, ageRange, themeIds, pagingRequest.getSkip(), pagingRequest.getSize());
        int total = searchMapper.countItemsByKeyword(keyword, minPrice, maxPrice, ageRange, themeIds);
        return new PageResponse<>(items, total, pagingRequest.getPageNo(), pagingRequest.getSize());
    }

    public List<Theme> getAllThemes() {
        return themeMapper.findAllThemes();
    }

    public Long getMemberIdByEmail(String email) {
        return memberMapper.findMemberIdByEmail(email);
    }
}
