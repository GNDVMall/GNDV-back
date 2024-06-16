package com.gndv.search.mapper;

import com.gndv.item.domain.entity.Item;
import com.gndv.search.domain.entity.Search;
import com.gndv.search.domain.request.SearchItemRequest;
import com.gndv.search.provider.SearchProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface SearchMapper {

    @Insert("INSERT INTO Search_Input(member_id, search_input) VALUES(#{member_id}, #{searchInput})")
    void insertSearchInput(Long member_id, String searchInput);

    @Select("SELECT search_input FROM Search_Input WHERE member_id = #{member_id} ORDER BY created_at DESC LIMIT 10")
    List<String> findRecentSearchesByMemberId(Long member_id);

    @Select("SELECT keyword FROM Search ORDER BY search_count DESC LIMIT 10")
    List<String> findPopularSearches();

    @Select("SELECT * FROM Search WHERE keyword = #{keyword}")
    Optional<Search> findSearchByKeyword(String keyword);

    @Update("UPDATE Search SET search_count = search_count + 1 WHERE keyword = #{keyword}")
    void updateSearchCount(String keyword);

    @Insert("INSERT INTO Search(keyword, search_count) VALUES(#{keyword}, 1)")
    void insertSearch(String keyword);

    @SelectProvider(type = SearchProvider.class, method = "findItemsByKeyword")
    List<SearchItemRequest> findItemsByKeyword(
            @Param("keyword") String keyword,
            @Param("sortBy") String sortBy,
            @Param("sortOrder") String sortOrder,
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            @Param("ageRange") String ageRange,
            @Param("themeIds") List<Long> themeIds,
            @Param("skip") int skip,
            @Param("size") int size);

    @SelectProvider(type = SearchProvider.class, method = "countItemsByKeyword")
    int countItemsByKeyword(
            @Param("keyword") String keyword,
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            @Param("ageRange") String ageRange,
            @Param("themeIds") List<Long> themeIds);
}
