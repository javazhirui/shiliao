package com.linghe.shiliao.entity.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PasswordDto implements Serializable {
    private static final long serialVersionUID = 6689268681595773549L;

    /**
     * 原密码
     */
    private String oldPassword;

    /**
     * 新密码
     */
    private String newPassword;
}
