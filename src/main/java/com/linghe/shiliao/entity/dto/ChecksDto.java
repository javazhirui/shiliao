package com.linghe.shiliao.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class ChecksDto implements Serializable {
    private static final long serialVersionUID = -5887561633767236815L;

    private Integer pageSize;
    private Integer currentPage;
    private Integer startSize;

    /**
     * 审核id
     */
    @TableId(value = "check_id", type = IdType.ASSIGN_ID)
    private Integer checksId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 自述病情
     */
    private String sympyom;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 处理时间
     */
    private String updateTime;

    /**
     * 处理状态,默认未处理-0 已处理-1
     */
    private Integer status;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 用户年龄
     */
    private Integer age;

    /**
     * 0未知 1男 2女
     */
    private String gender;

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
     * 用户账号
     */
    private String userName;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 权限id
     */
    private Integer ruleId;

    /**
     * 备注
     */
    private String remark;
}
