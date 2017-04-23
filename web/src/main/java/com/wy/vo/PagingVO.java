package com.wy.vo;

import java.util.List;

public class PagingVO<T> {
    private Integer total;
    private List<T> data;

    public PagingVO() {
    }

    public PagingVO(Integer total, List<T> data) {
        this.data = data;
        this.total = total;
    }

    public List<T> getData() {
        return this.data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Integer getTotal() {
        return this.total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
