package com.linghe.shiliao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;

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
public class Cases implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 病例id
     */
    @TableId(value = "cases_id", type = IdType.ASSIGN_ID)
    private Integer casesId;

    /**
     * 关联user_id
     */
    private Integer userId;

    /**
     * 诊断结果
     */
    private String diagnosis;

    /**
     * 处理意见
     */
    private String feedback;

    /**
     * 食疗视频路径
     */
    private String casesUrl;

    /**
     * 病例图片路径
     */
    private String casesImgUrl;

    /**
     * 创建时间
     */
    private String createTime;

}
