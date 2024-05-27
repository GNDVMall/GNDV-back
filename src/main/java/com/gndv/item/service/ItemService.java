package com.gndv.item.service;

import com.gndv.item.domain.dto.response.ItemDetailResponse;
import com.gndv.item.domain.entity.Item;
import com.gndv.item.mapper.ItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemMapper itemMapper;

    public ItemDetailResponse getItem(Long item_id) throws Exception {
        Optional<ItemDetailResponse> findById = itemMapper.findById(item_id);

        if (findById.isPresent()) {
            return findById.get();
        }
        throw new Exception();
    }

//    public List<ItemResponse> getItemList() {
//        Optional<List<Item>> findList = itemMapper.findList();
//    }
}
