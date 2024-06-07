package com.gndv.item.domain.dto.response;

import com.gndv.item.domain.entity.Item;
import lombok.Getter;

@Getter
public class ItemDetailResponse extends Item {
    private String real_filename;
    private String theme_name;
}
