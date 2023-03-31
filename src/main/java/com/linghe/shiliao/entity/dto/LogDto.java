package com.linghe.shiliao.entity.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class LogDto implements Serializable {

    private static final long serialVersionUID = -1752955134550333693L;

    private String ids;
    private String name;
    private String phone;
    private String email;
    private String startTime;
    private String endTime;
    /**
     * 操作类型 增/删/改
     */
    private String logType;

    /**
     * 具体操作
     */
    private String logMessage;

    private Integer pageSize;
    private Integer currentPage;
}
