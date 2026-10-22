package com.yujing.common;

import lombok.Data;

import java.util.List;

/**
 * 通用分页结果，供景点榜等列表接口使用
 */
@Data
public class PageResult<T> {
    private List<T> list;
    private long total;
    private int page;
    private int size;
    private int pages;

    public static <T> PageResult<T> of(List<T> list, long total, int page, int size) {
        PageResult<T> r = new PageResult<>();
        r.setList(list);
        r.setTotal(total);
        r.setPage(page);
        r.setSize(size);
        r.setPages(size > 0 ? (int) Math.ceil((double) total / size) : 0);
        return r;
    }
}
