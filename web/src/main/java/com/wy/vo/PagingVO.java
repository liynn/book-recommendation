package com.wy.vo;

import java.util.List;

public class PagingVO<T> {
    private Integer pageNo;
    private Integer pageSize;
    private Integer total;
    private Integer pageNumber;
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

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber,Integer pageSize) {
        this.pageNumber = (total + pageSize - 1) / pageSize;
    }
}
