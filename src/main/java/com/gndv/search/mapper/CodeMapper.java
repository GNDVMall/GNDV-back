package com.gndv.search.mapper;

import com.gndv.search.domain.entity.Code;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CodeMapper {

    @Select("SELECT * FROM Code WHERE code_type = #{codeType}")
    List<Code> findByCodeType(@Param("codeType") String codeType);
}
