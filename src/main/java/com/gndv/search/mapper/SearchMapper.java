package com.gndv.search.mapper;

import com.gndv.item.domain.entity.Item;
import com.gndv.search.domain.entity.Search;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
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

    @Select("SELECT * FROM Item WHERE item_name LIKE CONCAT('%', #{keyword}, '%')")
    List<Item> findItemsByKeyword(String keyword);
}
