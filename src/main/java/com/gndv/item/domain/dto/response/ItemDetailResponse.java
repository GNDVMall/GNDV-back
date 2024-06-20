package com.gndv.item.domain.dto.response;

import com.gndv.item.domain.entity.Item;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ItemDetailResponse extends Item {
    private String theme_name;
    private Long wish_count;
}
