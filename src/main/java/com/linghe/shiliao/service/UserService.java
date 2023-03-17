package com.linghe.shiliao.service;


import com.linghe.shiliao.common.R;
import com.linghe.shiliao.entity.UserMessage;
import com.linghe.shiliao.entity.dto.UserMessageDto;

public interface UserService {

    /**
     * 注册
     * @param userMessageDto
     * @return
     */
    R<String> register(UserMessageDto userMessageDto);
}
