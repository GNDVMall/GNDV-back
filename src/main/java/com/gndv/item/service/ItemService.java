package com.gndv.item.service;

import com.gndv.image.domain.entity.Image;
import com.gndv.item.domain.dto.ItemResponse;
import com.gndv.item.domain.entity.Item;
import com.gndv.item.mapper.ItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemMapper itemMapper;

    public ItemResponse getItem(Long item_id) throws Exception {
        Optional<Item> findById = itemMapper.findById(item_id);
        Optional<Image> image = itemMapper.findImages(item_id, "item");

        if (findById.isPresent()) {
            return new ItemResponse(findById.get(), image.get());
        }
        throw new Exception();
    }
}
