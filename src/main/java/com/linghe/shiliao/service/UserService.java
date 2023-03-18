package com.linghe.shiliao.service;


import com.linghe.shiliao.common.R;
import com.linghe.shiliao.entity.dto.LoginDto;
import com.linghe.shiliao.entity.dto.UserMessageDto;

import java.io.IOException;

public interface UserService {

    /**
     * 注册
     * @param userMessageDto
     * @return
     */
    R<String> register(UserMessageDto userMessageDto);

    /**
     * 获取验证码
     * @param uuid
     * @return
     * @throws IOException
     */
    R<String> getCode(String uuid) throws IOException;

    R<String> login(LoginDto loginDto);
}
