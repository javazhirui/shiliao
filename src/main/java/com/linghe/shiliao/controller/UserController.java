package com.linghe.shiliao.controller;

import com.linghe.shiliao.common.R;
import com.linghe.shiliao.entity.dto.LoginDto;
import com.linghe.shiliao.entity.dto.UserMessageDto;
import com.linghe.shiliao.service.MailService;
import com.linghe.shiliao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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

    /**
     * 注册
     *
     * @param userMessageDto
     * @return
     */
    @PostMapping("/register")
    public R<String> register(@RequestBody UserMessageDto userMessageDto) {
        return userService.register(userMessageDto);
    }

    /**
     * 登录方法 jwt
     * @param loginDto
     * @return 返回一个token
     */
    @PostMapping("/login")
    public R<String> login(@RequestBody LoginDto loginDto) {
        return userService.login(loginDto);
    }


    /**
     * 获取验证码
     *
     * @param uuid //前端页面返回唯一的uuId
     * @return //返回验证码路径
     * @throws IOException
     */
    @GetMapping("/getCode")
    public R<String> getCode(@RequestParam String uuid) throws IOException {
        return userService.getCode(uuid);
    }

    /**
     * 发送邮箱验证码
     *
     * @param uuid
     * @param email
     * @return
     */
    @GetMapping("/getEmailCode")
    public R<String> getEmailCode(@RequestParam String uuid, @RequestParam String email) {
        return mailService.getEmailCode(uuid, email);
    }


}
