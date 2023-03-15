package com.linghe.shiliao.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.linghe.shiliao.entity.UserMessage;
import com.linghe.shiliao.entity.dto.PageRequestDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserMessageVo extends PageRequestDto {

    private static final long serialVersionUID = 3690068092366244828L;
    //年龄范围
    private Integer minAge;
    private Integer maxAge;

    //时间范围
    private Integer minCreatetime;
    private Integer maxCreatetime;
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 用户年龄
     */
    private Integer age;

    /**
     * 0/未知(1男 2女)
     */
    private Integer gender;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 身体健康情况
     */
    private String health;

    /**
     * 用户账号(默认为手机号)
     */
    private String userName;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 0停用 1启用
     */
    private Integer status;

    /**
     * 病例id
     */
    private String caseId;

    /**
     * 权限id
     */
    private Integer ruleId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

}
