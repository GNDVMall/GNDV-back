package com.gndv.item.domain.dto;

import com.gndv.image.domain.entity.Image;
import com.gndv.item.domain.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemResponse{
    private Item item;
    private Image item_images;
}
