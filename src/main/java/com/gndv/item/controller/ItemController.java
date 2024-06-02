package com.gndv.item.controller;

import com.gndv.common.CustomResponse;
import com.gndv.item.domain.dto.response.ItemDetailResponse;
import com.gndv.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/{item_id}")
    public CustomResponse<ItemDetailResponse> getItem(@PathVariable Long item_id) throws Exception {
        log.info("Get One Item by id {}", item_id);
        ItemDetailResponse findItem = itemService.getItem(item_id);
        return CustomResponse.ok("Get a Item", findItem);
    }

    @GetMapping("")
    public CustomResponse<List<ItemDetailResponse>> getItemList() throws Exception {
        log.info("Get All Item List");
        List <ItemDetailResponse>  items = itemService.getItems();
        log.info("items {}", items);
        return CustomResponse.ok("Get Items", items);
    }
}
