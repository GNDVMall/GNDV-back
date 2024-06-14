package com.gndv.search.mapper;

import com.gndv.search.domain.entity.Theme;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ThemeMapper {

    @Select("SELECT * FROM Lego_Theme")
    List<Theme> findAllThemes();
}
