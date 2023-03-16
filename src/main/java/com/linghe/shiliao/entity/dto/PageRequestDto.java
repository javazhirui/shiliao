package com.linghe.shiliao.entity.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Data
@Slf4j
public class PageRequestDto implements Serializable {

    private Integer pageSize;
    private Integer currentPage;
    private Integer startSize = (currentPage - 1) * pageSize;

    public void checkParam() {
        if (this.currentPage == null || this.currentPage < 0) {
            setCurrentPage(1);
        }
        if (this.pageSize == null || this.pageSize < 0 || this.pageSize > 100) {
            setPageSize(10);
        }
    }
}
