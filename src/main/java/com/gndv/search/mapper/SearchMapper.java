package com.gndv.search.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SearchMapper {

    @Insert("INSERT INTO Search (keyword, search_count) VALUES (#{keyword}, 1) " +
            "ON DUPLICATE KEY UPDATE search_count = search_count + 1")
    void insertOrUpdateSearch(@Param("keyword") String keyword);

    @Select("SELECT search_input FROM Search_Input ORDER BY created_at DESC LIMIT 10")
    List<String> findRecentSearches();

    @Select("SELECT keyword FROM Search ORDER BY search_count DESC LIMIT 10")
    List<String> findPopularSearches();
}
