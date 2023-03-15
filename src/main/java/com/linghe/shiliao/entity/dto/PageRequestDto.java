package com.linghe.shiliao.entity.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class PageRequestDto {

    protected Integer size;
    protected Integer currentPage;

    public void checkParam() {
        if (this.currentPage == null || this.currentPage < 0) {
            setCurrentPage(1);
        }
        if (this.size == null || this.size < 0 || this.size > 100) {
            setSize(10);
        }
    }
}
