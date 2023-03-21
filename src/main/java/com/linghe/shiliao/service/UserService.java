package com.linghe.shiliao.service;


import com.linghe.shiliao.common.R;
import com.linghe.shiliao.entity.dto.LoginDto;
import com.linghe.shiliao.entity.dto.UserMessageDto;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

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

    /**
     * 登录方法 jwt
     * @param loginDto
     * @return
     */
    R<String> login(LoginDto loginDto);
}
