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

    @Insert("INSERT INTO Search_Input(search_input) VALUES(#{searchInput})")
    void insertSearchInput(String searchInput);

    @Select("SELECT search_input FROM Search_Input ORDER BY created_at DESC LIMIT 10")
    List<String> findRecentSearches();

    @Select("SELECT keyword FROM Search ORDER BY search_count DESC LIMIT 10")
    List<String> findPopularSearches();

    @Select("SELECT * FROM Search WHERE keyword = #{keyword}")
    Optional<Search> findSearchByKeyword(String keyword);

    @Update("UPDATE Search SET search_count = search_count + 1 WHERE keyword = #{keyword}")
    void updateSearchCount(String keyword);

    @Insert("INSERT INTO Search(keyword, search_count) VALUES(#{keyword}, 1)")
    void insertSearch(String keyword);

    @Results(id = "itemWithImageResultMap", value = {
            @Result(column = "item_id", property = "item_id"),
            @Result(column = "item_number", property = "item_number"),
            @Result(column = "regular_price", property = "regular_price"),
            @Result(column = "age_range", property = "age_range"),
            @Result(column = "pieces", property = "pieces"),
            @Result(column = "item_name", property = "item_name"),
            @Result(column = "recent_price", property = "recent_price"),
            @Result(column = "release_date", property = "release_date"),
            @Result(column = "wish_count", property = "wish_count"),
            @Result(column = "view_count", property = "view_count"),
            @Result(column = "theme_id", property = "theme_id"),
            @Result(column = "recommend", property = "recommend"),
            @Result(column = "image_url", property = "image_url"),
            @Result(column = "theme_name", property = "theme_name")
    })
    @SelectProvider(type = SearchProvider.class, method = "findItemsByKeyword")
    List<SearchItemRequest> findItemsByKeyword(
            @Param("keyword") String keyword,
            @Param("sortBy") String sortBy,
            @Param("sortOrder") String sortOrder,
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            @Param("ageRange") String ageRange,
            @Param("themeIds") List<Long> themeIds);
}
