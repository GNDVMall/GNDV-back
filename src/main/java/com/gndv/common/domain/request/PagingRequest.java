package com.gndv.common.domain.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagingRequest {
    @Min(value = 1)
    @Positive
    private int pageNo = 1;

    @Min(value = 10)
    @Max(value = 100)
    @Positive
    private int size = 10;

    private String searchKey;

    public int getSkip(){ // sql에 쓸 데이터의 시작 위치
        return (pageNo - 1) * size;
    }
}
