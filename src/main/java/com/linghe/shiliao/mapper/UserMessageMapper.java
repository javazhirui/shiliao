package com.linghe.shiliao.mapper;

import com.linghe.shiliao.entity.UserMessage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linghe.shiliao.entity.vo.UserMessageVo;
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

    List<UserMessage> getList(UserMessageVo userVo);

    long getTotal();
}
