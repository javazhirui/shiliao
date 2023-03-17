package com.linghe.shiliao.service.impl;

import com.linghe.shiliao.entity.UserMessage;
import com.linghe.shiliao.entity.dto.UserMessageDto;
import com.linghe.shiliao.mapper.UserMessageMapper;
import com.linghe.shiliao.service.UserMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linghe.shiliao.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhao_qin
 * @since 2023-03-14
 */
@Service
public class UserMessageServiceImpl extends ServiceImpl<UserMessageMapper, UserMessage> implements UserMessageService {

    @Autowired
    private UserMessageMapper userMessageMapper;

    /**
     * 查询用户信息
     *
     * @param userMessageDto
     * @return
     */
    @Override
    public Page<UserMessage> getList(UserMessageDto userMessageDto) {
        Page<UserMessage> page = new Page<>();
        page.setTotal(userMessageMapper.getTotal());
        page.setList(userMessageMapper.getList(userMessageDto));
        page.setCurrentPage(userMessageDto.getCurrentPage());
        page.setPageSize(userMessageDto.getPageSize());
        return page;
    }
}
