package com.linghe.shiliao.utils;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <b> 分页通用类 </b>
 *
 * @param <T>
 */
@Component
public class Page<T> {

    private List<T> list;  //数据
    private int PageSize;  //每页条数
    private int currentPage;  //当前页
    private long total;  //总条数
    private long totalPage; //总页数
    private long begin;
    private long end;


    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getPageSize() {
        return PageSize;
    }

    public void setPageSize(int PageSize) {
        this.PageSize = PageSize;
    }

    public int getCurrentPage(Integer currentPage) {
        return this.currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public long getBegin() {
        return begin;
    }

    public void setBegin(long begin) {
        this.begin = begin;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
        // 总页数
        this.totalPage = this.total % this.PageSize > 0 ? this.total / this.PageSize + 1
                : this.total / this.PageSize;
    }

    public void firstFiveAndLastFour() {

        setBegin(currentPage - 5 < 1 ? 1 : currentPage - 5);

        if (currentPage - 5 >= 1 && currentPage + 4 < getTotalPage()) {
            setEnd(currentPage + 4);
        } else if (currentPage - 5 >= 1 && currentPage + 4 > getTotalPage()) {
            setBegin(getTotalPage() - 9 < 1 ? 1 : getTotalPage() - 9);
            setEnd(getTotalPage());
        } else {
            setEnd(10);
        }
    }

    @Override
    public String toString() {
        return "Page [list=" + list + ", PageSize=" + PageSize + ", currentPage=" + currentPage + ", total=" + total
                + ", totalPage=" + totalPage + ", begin=" + begin + ", end=" + end + "]";
    }


}
