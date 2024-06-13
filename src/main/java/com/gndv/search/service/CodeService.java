package com.gndv.search.service;

import com.gndv.search.domain.entity.Code;
import com.gndv.search.mapper.CodeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CodeService {

    private final CodeMapper codeMapper;

    public List<Code> getCodesByType(String codeType) {
        return codeMapper.findCodesByType(codeType);
    }
}
