package com.chf.lightjob.model;

/**
 * @description
 * @author: Davy
 * @create: 2020-10-05 13:11
 */
public class PageRequest {

    Integer pageNo;

    Integer pageSize;

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        if (pageNo != null && pageNo < 1) {
            this.pageNo = 1;
            return;
        }
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getStartRow() {
        if(pageNo == null || pageSize == null){
            return null;
        }

        return (pageNo - 1) * pageSize;
    }
}
