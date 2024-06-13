package com.gndv.image.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ImageMapper {
    @Delete({
            "<script>",
            "DELETE FROM Image",
            "WHERE real_filename IN",
            "<foreach item='real_filename' collection='list' open='(' separator=',' close=')'>",
            "#{real_filename}",
            "</foreach>",
            "</script>"
    })
    void deleteImages(@Param("list") List<String> urls);
}
