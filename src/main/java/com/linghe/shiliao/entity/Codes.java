package com.linghe.shiliao.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Codes implements Serializable {
    private static final long serialVersionUID = 5354420354865412394L;

    /**
     * id(根据md5计算的)
     */
    private String id;

    /**
     * 图片验证code
     */
    private String code;


}
