package com.gndv.common.domain.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PageResponse<E> {
    private int pageNo;
    private int size;
    private int total;

    private int start;
    private int end;

    private boolean prev;
    private boolean next;

    private List<E> list;

    @Builder
    public PageResponse(List<E> list, int total, int pageNo, int size){
        this.pageNo = pageNo;
        this.size = size;
        this.total = total;
        this.list = list;

        this.end =   (int)(Math.ceil(this.pageNo / 10.0 )) * this.size;
        this.start = this.end - 9;

        int last =  (int)(Math.ceil((total/(double)size)));

        this.end =  end > last ? last: end;
        this.prev = this.start > 1;

        this.next =  total > this.end * this.size;
    }
}
