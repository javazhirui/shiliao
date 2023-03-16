package com.linghe.shiliao.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.linghe.shiliao.entity.UserMessage;
import com.linghe.shiliao.entity.dto.PageRequestDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Data
public class UserMessageVo extends UserMessage implements Serializable {

    //年龄范围
    private Integer minAge;
    private Integer maxAge;

    //时间范围
    private String minCreatetime;
    private String maxCreatetime;

    private Integer pageSize;
    private Integer currentPage;
    private Integer startSize = (currentPage - 1) * pageSize;

}
