package com.gndv.search.controller;

import com.gndv.search.domain.entity.Code;
import com.gndv.search.service.CodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v2/code")
@RequiredArgsConstructor
public class CodeController {

    private final CodeService codeService;

    @GetMapping("/{codeType}")
    public ResponseEntity<List<Code>> getCodesByType(@PathVariable String codeType) {
        List<Code> codes = codeService.getCodesByType(codeType);
        return ResponseEntity.ok(codes);
    }
}
