package com.linghe.shiliao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linghe.shiliao.common.R;
import com.linghe.shiliao.entity.UserMessage;
import com.linghe.shiliao.entity.dto.UserMessageDto;
import com.linghe.shiliao.utils.Page;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhao_qin
 * @since 2023-03-14
 */
public interface UserMessageService extends IService<UserMessage> {

    /**
     * 查询用户信息
     * @param userMessageDto
     * @return
     */
    Page<UserMessage> getList(UserMessageDto userMessageDto);

    void editUserMessageBean(UserMessage userMessageDto);

    void delUserMessage(UserMessage userMessage);
}
