package com.linghe.shiliao.entity.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginDto implements Serializable {

    private static final long serialVersionUID = 3306579745488982863L;

    /**
     * 账号(暂定邮箱作为账号)
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 验证码
     */
    private String code;

    /**
     * 前端唯一uuid
     */
    private String uuid;

    /**
     * 退出账户userid
     */
    private String userId;

}
