package com.lzq.selfdiscipline.business.bean;

import java.util.List;

/**
 * 分页 bean
 */
public class PageBean<T> {
    // 当前页码
    private Integer page;
    // 每页条数
    private Integer pageSize;
    // 总条数
    private Integer total;
    // 数据
    private List<T> list;

    public PageBean() {
    }

    public PageBean(Integer page, Integer pageSize, Integer total, List<T> list) {
        this.page = page;
        this.pageSize = pageSize;
        this.total = total;
        this.list = list;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
