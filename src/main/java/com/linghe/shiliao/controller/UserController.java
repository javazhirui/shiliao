package com.linghe.shiliao.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linghe.shiliao.aop.RuleAop;
import com.linghe.shiliao.common.R;
import com.linghe.shiliao.entity.UserMessage;
import com.linghe.shiliao.entity.dto.LoginDto;
import com.linghe.shiliao.entity.dto.UserMessageDto;
import com.linghe.shiliao.service.MailService;
import com.linghe.shiliao.service.UserMessageService;
import com.linghe.shiliao.service.UserService;
import com.linghe.shiliao.utils.JwtUtils;
import com.linghe.shiliao.utils.RedisCache;
import com.linghe.shiliao.utils.SMSUtils;
import com.linghe.shiliao.utils.VerifyCodeUtils;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sun.rmi.runtime.Log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

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
    private RedisCache redisCache;

    @Autowired
    private UserMessageService userMessageService;

    @Value("${jwtDeadTime}")
    private String jwtDeadTime;

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
//    @RuleAop(ruleNames = {"zilongcs"})
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
//        String uuid, @RequestParam String email, @RequestParam String code
        return mailService.getEmailCode(userMessageDto.getUuid(), userMessageDto.getEmail(), userMessageDto.getCode());
    }

    /**
     * 忘记密码,修改密码
     *
     * @return
     */
    @PostMapping("/forgetPassword")
    public R<String> forgetPassword(@RequestBody UserMessageDto userMessageDto) {
        return userService.forgetPassword(userMessageDto);
    }



    /**
     * 登录前,查询登录状态
     *
     * @param loginDto
     * @return
     */
    @PostMapping("/getLoginStatus")
    public R<String> getLoginStatus(@RequestBody LoginDto loginDto) {
        LambdaQueryWrapper<UserMessage> lqw = new LambdaQueryWrapper<>();
        lqw.eq(UserMessage::getUserName, loginDto.getUserName());
        UserMessage userMessage = userMessageService.getOne(lqw);
        if (ObjectUtils.isEmpty(userMessage)) {
            LambdaQueryWrapper<UserMessage> lqw1 = new LambdaQueryWrapper<>();
            lqw1.eq(UserMessage::getEmail,loginDto.getUserName());
            userMessage = userMessageService.getOne(lqw1);
            if (ObjectUtils.isEmpty(userMessage)) {
                return R.error("登录账号不存在");
            }
        }
        String loginUuid = redisCache.getCacheObject("login_" + userMessage.getUserId());
        if (loginUuid != null && !loginDto.getUuid().equals(loginUuid) && !loginUuid.equals("logout")) {
            return R.error("该账号已在其它设备登录,登录将会导致其它设备强制退出");
        }
        return R.success("无其他设备登录,可正常登录");
    }

    /**
     * 退出登录
     * @param loginDto
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request,@RequestBody LoginDto loginDto) {
        String userIdByJwtToken = JwtUtils.getUserIdByJwtToken(request);
        if (StringUtils.isEmpty(loginDto.getUserId()) && !StringUtils.equals(userIdByJwtToken,loginDto.getUserId())) {
            return R.error("userId错误,只能退出当前验证登录账号");
        }
        redisCache.setCacheObject("login_" + loginDto.getUserId(), "logout", Integer.parseInt(jwtDeadTime), TimeUnit.SECONDS);
        return R.success("退出成功");
    }

    @PostMapping("/getPhoneCode")
    public R<String> getPhoneCode() {
        try {
            SMSUtils.sendMessage("阿里云短信测试", "SMS_154950909", "17731815890", VerifyCodeUtils.generateVerifyCode(6));
        } catch (Exception e) {
            R.error("获取验证码意外失败");
        }
        return R.success("短信验证码发送成功");
    }
}
