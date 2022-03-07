package com.chf.lightjob.model;

import java.util.List;

/**
 * @Author ChenHaifeng
 * @Date 2018/9/2 下午12:31
 */
public class PageData<T> {
    /**
     * 当totalSize跟pageNo为null时，用于循环查询下一页，直到list.size()小于查询的pageSize
     */
    private Integer totalSize;
    private List<T> list;

    @Deprecated
    public PageData(Integer pageNo, int pageSize) {
        //this.pageNo = pageNo;
        //this.pageSize = pageSize;
    }

    public PageData() {}

    private PageData(Integer totalSize, List<T> list) {
        this.totalSize = totalSize;
        this.list = list;
    }

    public Integer getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Integer totalSize) {
        this.totalSize = totalSize;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public static <T> PageData<T> emptyPage(int totalSize) {
        return new PageData(totalSize, null);
    }

    public static <T> PageData<T> listPage(int totalSize, List<T> list) {
        return new PageData(totalSize, list);
    }
}
