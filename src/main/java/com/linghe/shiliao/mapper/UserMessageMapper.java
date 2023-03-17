package com.linghe.shiliao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linghe.shiliao.entity.UserMessage;
import com.linghe.shiliao.entity.dto.UserMessageDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhao_qin
 * @since 2023-03-14
 */
@Mapper
public interface UserMessageMapper extends BaseMapper<UserMessage> {

    List<UserMessage> getList(UserMessageDto userVo);

    long getTotal();
}
