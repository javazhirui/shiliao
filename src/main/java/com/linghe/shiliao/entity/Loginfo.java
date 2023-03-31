package com.linghe.shiliao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Loginfo implements Serializable {
    private static final long serialVersionUID = -1377294564252722187L;

    /**
     * 操作日志id
     */
    @TableId(value = "loginfo_id", type = IdType.ASSIGN_ID)
    private Long loginfoId;

    /**
     * 请求路径
     */
    private String requestUrl;

    /**
     * (备用字段) 操作者ip地址
     */
    private String userIp;

    /**
     * 操作用户id
     */
    private String userId;

    /**
     * 操作类型 增/删/改
     */
    private String logType;

    /**
     * 具体操作
     */
    private String logMessage;

    /**
     * 请求参数  一个集合转string
     */
    private String request;

    /**
     * 返回参数  可保存失败信息
     */
    private String response;

    /**
     * 操作时间
     */
    private String createTime;


}
