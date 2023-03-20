package com.linghe.shiliao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhao_qin
 * @since 2023-03-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Checks implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 审核id
     */
    @TableId(value = "checks_id", type = IdType.ASSIGN_ID)
    private Integer checksId;

    /**
     * 用户id
     */
    private Integer userId;

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


}
