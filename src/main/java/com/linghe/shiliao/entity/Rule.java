package com.linghe.shiliao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
public class Rule implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 权限id
     */
    @TableId(value = "rule_id", type = IdType.ASSIGN_ID)
    private Integer ruleId;

    /**
     * 权限名称
     */
    private String ruleName;


}
