package com.linghe.shiliao.controller;

import com.linghe.shiliao.aop.RuleAop;
import com.linghe.shiliao.common.R;
import com.linghe.shiliao.entity.dto.LoginDto;
import com.linghe.shiliao.entity.dto.UserMessageDto;
import com.linghe.shiliao.service.MailService;
import com.linghe.shiliao.service.UserMessageService;
import com.linghe.shiliao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 用户注册、登录、修改权限
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @Autowired
    private UserMessageService userMessageService;

    /**
     * 注册
     *
     * @param userMessageDto
     * @return
     */
    @PostMapping("/register")
    public R<String> register(UserMessageDto userMessageDto) {
        return userService.register(userMessageDto);
    }

    /**
     * 登录方法 jwt
     *
     * @param loginDto
     */
    @PostMapping("/login")
    public R<String> login(HttpServletResponse response, @RequestBody LoginDto loginDto) {
        return userService.login(response, loginDto);
    }


    /**
     * 获取验证码
     *
     * @param uuid //前端页面返回唯一的uuId
     * @return //返回验证码输出字节流
     * @throws IOException
     */
    @RuleAop(ruleNames = {"zilongcs"})
    @GetMapping("/getCode")
    public R<String> getCode(String uuid) throws IOException {
        return userService.getCode(uuid);
    }

    /**
     * 发送邮箱验证码
     *
     * @param userMessageDto
     * @return
     */
    @PostMapping("/getEmailCode")
    public R<String> getEmailCode(@RequestBody UserMessageDto userMessageDto) {
        System.err.println("soleUUid:" + userMessageDto.getUuid());
//        String uuid, @RequestParam String email, @RequestParam String code
        return mailService.getEmailCode(userMessageDto.getUuid(), userMessageDto.getEmail(), userMessageDto.getCode());
    }

    /**
     * 忘记密码,修改密码
     *
     * @return
     */
    @PostMapping("/forgetPassword")
    public R<String> forgetPassword(UserMessageDto userMessageDto) {
        return userService.forgetPassword(userMessageDto);
    }


}
