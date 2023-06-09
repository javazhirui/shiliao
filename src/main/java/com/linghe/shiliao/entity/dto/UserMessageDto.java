package com.linghe.shiliao.entity.dto;

import com.linghe.shiliao.entity.UserMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Data
public class UserMessageDto extends UserMessage implements Serializable {

    //图片验证码
    private String uuid;
    private String code;
    //邮箱验证码
    private String emailCode;

    //手机验证码--备用
    private String phoneCode;

    //年龄范围
    private Integer minAge;
    private Integer maxAge;

    //时间范围
    private String minCreateTime;
    private String maxCreateTime;

    private Integer pageSize;
    private Integer currentPage;

}
