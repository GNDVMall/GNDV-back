package com.gndv.item.service;

import com.gndv.item.domain.entity.Item;
import com.gndv.item.mapper.ItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemMapper itemMapper;

    public Item getItem(Long item_id) throws Exception {
        Optional<Item> findById = itemMapper.findById(item_id);

        if (findById.isPresent()) {
            return findById.get();
        }
        throw new Exception();
    }
}
