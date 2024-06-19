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
    @Builder.Default
    private int pageNo = 1;

    @Min(value = 10)
    @Max(value = 100)
    @Positive
    @Builder.Default
    private int size = 10;

    private String searchKey;

    public int getSkip() {
        return (pageNo - 1) * size;
    }

    public PagingRequest(int pageNo, int size) {
        this.pageNo = pageNo;
        this.size = size;
    }
}
