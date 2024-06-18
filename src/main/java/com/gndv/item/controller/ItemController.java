package com.gndv.item.controller;

import com.gndv.common.CustomResponse;
import com.gndv.item.domain.dto.response.ItemDetailResponse;
import com.gndv.item.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/items")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Item API", description = "아이템 관련 API")
public class ItemController {

    private final ItemService itemService;

    @Operation(summary = "아이템 조회", description = "ID를 통해 특정 아이템을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "아이템 조회 성공", content = @io.swagger.v3.oas.annotations.media.Content(schema = @Schema(implementation = ItemDetailResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/{item_id}")
    public CustomResponse<ItemDetailResponse> getItem(
            @Parameter(description = "아이템 ID", required = true) @PathVariable Long item_id) throws Exception {
        log.info("Get One Item by id {}", item_id);
        ItemDetailResponse findItem = itemService.getItem(item_id);
        return CustomResponse.ok("Get an Item", findItem);
    }

    @Operation(summary = "아이템 목록 조회", description = "모든 아이템 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "목록 조회 성공", content = @io.swagger.v3.oas.annotations.media.Content(schema = @Schema(implementation = ItemDetailResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("")
    public CustomResponse<List<ItemDetailResponse>> getItemList() throws Exception {
        log.info("Get All Item List");
        List<ItemDetailResponse> items = itemService.getItems();
        log.info("items {}", items);
        return CustomResponse.ok("Get Items", items);
    }
}
