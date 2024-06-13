package com.gndv.search.controller;

import com.gndv.search.domain.entity.Code;
import com.gndv.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v2/code")
@RequiredArgsConstructor
public class CodeController {

    private final SearchService searchService;

    @GetMapping("/category")
    public ResponseEntity<List<Code>> getCategoryCodes() {
        List<Code> codes = searchService.getCategoryCodes();
        return ResponseEntity.ok(codes);
    }
}
