package com.gndv.search.mapper;

import com.gndv.search.domain.entity.Theme;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ThemeMapper {

    @Select("SELECT * FROM Lego_Theme\n" +
            "WHERE theme_image_url IS NOT NULL\n" +
            "AND theme_image_url != ''")
    List<Theme> findAllThemes();
}
