package com.linghe.shiliao.service;


import com.linghe.shiliao.common.R;
import com.linghe.shiliao.entity.dto.LoginDto;
import com.linghe.shiliao.entity.dto.UserMessageDto;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface UserService {

    /**
     * 注册
     *
     * @param userMessageDto
     * @return
     */
    R<String> register(UserMessageDto userMessageDto);

    /**
     * 获取验证码
     *
     * @param uuid
     * @return
     * @throws IOException
     */
    R<String> getCode(String uuid) throws IOException;

    /**
     * 登录方法 jwt
     *
     * @param loginDto
     * @return
     */
    R<String> login(HttpServletResponse response, LoginDto loginDto);

    /**
     * 忘记密码,修改密码
     *
     * @return
     */
    R<String> forgetPassword(UserMessageDto userMessageDto);

    R<String> getCode1(String uuid) throws IOException;

    /**
     * 回传图片验证码图片流
     * @param uuid
     * @return
     * @throws IOException
     */
    R<String> getCode64(String uuid) throws IOException;

    /**
     * 发送手机验证码
     * @param uuid
     * @param phone
     * @return
     */
    R<String> getPhoneCode(String uuid, String phone);
}
