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
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

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

//    @PostMapping("/loginout")
//    pub

    /**
     * 登录前,查询登录状态
     *
     * @param loginDto
     * @return
     */
    @PostMapping("/getLoginStatus")
    public R<String> getLoginStatus(LoginDto loginDto) {
        LambdaQueryWrapper<UserMessage> lqw = new LambdaQueryWrapper<>();
        lqw.eq(UserMessage::getUserName, loginDto.getUserName());
        UserMessage userMessage = userMessageService.getOne(lqw);
        if (ObjectUtils.isEmpty(userMessage)) {
            return R.error("用户名不存在,请先注册");
        }
        String loginUuid = redisCache.getCacheObject("login_" + userMessage.getUserId());
        if (loginUuid != null && !loginDto.getUuid().equals(loginUuid)) {
            return R.error("该账号已在其它设备登录,登录将会导致其它设备强制退出");
        }
        return R.success("无其他设备登录,可正常登录");
    }

    /**
     * 退出登录
     * @param userId
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request, String userId) {
        String userIdByJwtToken = JwtUtils.getUserIdByJwtToken(request);
        if (StringUtils.isEmpty(userId) && !StringUtils.equals(userIdByJwtToken,userId)) {
            return R.error("userId错误,只能退出当前验证登录账号");
        }
        redisCache.setCacheObject("login_" + userId, "logout", Integer.parseInt(jwtDeadTime), TimeUnit.SECONDS);
        return R.success("退出成功");
    }
}
