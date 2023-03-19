package com.linghe.shiliao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linghe.shiliao.common.R;
import com.linghe.shiliao.entity.UserMessage;
import com.linghe.shiliao.entity.dto.LoginDto;
import com.linghe.shiliao.entity.dto.UserMessageDto;
import com.linghe.shiliao.mapper.CodesMapper;
import com.linghe.shiliao.mapper.UserMessageMapper;
import com.linghe.shiliao.service.UserService;
import com.linghe.shiliao.utils.Md5Utils;
import com.linghe.shiliao.utils.RedisCache;
import com.linghe.shiliao.utils.VerifyCodeUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMessageMapper userMessageMapper;

    @Autowired
    private CodesMapper codesMapper;

    //操作Redis工具类
    @Autowired
    private RedisCache redisCache;

    /**
     * 获取验证码
     *
     * @param uuid 前端回传唯一UUID
     * @return 返回验证码路径
     * @throws IOException
     */
    @Override
    public R<String> getCode(String uuid) throws IOException {
        if (StringUtils.isBlank(uuid)) {
            return R.error("页面uuId不可为空");
        }
        String code = VerifyCodeUtils.generateVerifyCode(6);
        String codeRedis = Md5Utils.hash(code);
        //code缓存到Redis
        redisCache.setCacheObject(uuid, codeRedis, 30, TimeUnit.SECONDS);

        String CodeImgPath = "D:/CodeImg/Code.jpg";//后期可换minio地址
        File file = new File(CodeImgPath);
        if (!file.getParentFile().exists()) { // 此时文件有父目录
            file.getParentFile().mkdirs(); // 创建父目录
        }
        //构造输出流
        OutputStream os = new FileOutputStream(file);
        //生成验证码图片  参数(验证码长,高,字节输出流,验证码)
        VerifyCodeUtils.outputImage(30, 15, os, code);
        os.close();
        return R.success(CodeImgPath);
    }

    /**
     * 登录方法
     *
     * @param loginDto 账号,密码,验证码,
     * @return 返回一个token
     */
    @Override
    public R<String> login(LoginDto loginDto) {
        String code = loginDto.getCode();
        String codeRedis = redisCache.getCacheObject(loginDto.getUuid());
        if (!StringUtils.equals(code, codeRedis)) {
            return R.error("验证码输入错误");
        }
        LambdaQueryWrapper<UserMessage> lqw = new LambdaQueryWrapper<>();
        lqw.eq(UserMessage::getEmail, loginDto.getUsername());
        lqw.eq(UserMessage::getPassword, Md5Utils.hash(loginDto.getPassword()));
        UserMessage userMessage = userMessageMapper.selectOne(lqw);
        if (ObjectUtils.isEmpty(userMessage)) {
            return R.error("账号或密码错误");
        }
        if (userMessage.getStatus() == 0) {
            return R.error("账号已停用,请联系管理员");
        }


        return null;
    }

    /**
     * 邮箱注册方法
     *
     * @param userMessageDto
     * @return
     */
    @Override
    public R<String> register(UserMessageDto userMessageDto) {
        try {
            if (ObjectUtils.isEmpty(userMessageDto)) {
                return R.error("数据为空,请检查");
            }
            if (userMessageDto.getEmail().isBlank()) {
                return R.error("邮箱为空");
            }
            //查询构造器
            LambdaQueryWrapper<UserMessage> lqw = new LambdaQueryWrapper<>();
            //查询email
            lqw.eq(UserMessage::getEmail, userMessageDto.getEmail());
            //获取第一个查询结果
            UserMessage user = userMessageMapper.selectOne(lqw);
            if (!ObjectUtils.isEmpty(user) && !ObjectUtils.isEmpty(user.getEmail())) {
                return R.error("邮箱已注册,请更换邮箱或重新登陆");
            }
            if (StringUtils.isBlank(userMessageDto.getCode())) {
                return R.error("验证码为空,请检查");
            }
            //根据页面唯一uuId获取缓存code(md5加密后)
            String codeRedis = redisCache.getCacheObject(userMessageDto.getUuid());
            //前端用户输入的验证码
            String code = userMessageDto.getCode();
            code = Md5Utils.hash(code);
            if (!StringUtils.equals(code, codeRedis)) {
                return R.error("验证码输入有误,请重新输入");
            }
            if (userMessageDto.getEmailCode().isBlank()) {
                return R.error("邮箱验证码为空,请检查");
            }
            String emailCode = userMessageDto.getEmailCode();
            emailCode = Md5Utils.hash(emailCode);
            //获取缓存的邮箱验证码(md5加密后的)
            String emailCodeRedis = redisCache.getCacheObject(userMessageDto.getEmail() + "_" + userMessageDto.getUuid());
            if (!StringUtils.equals(emailCode, emailCodeRedis)) {
                return R.error("邮箱验证码输入有误");
            }
            if (userMessageDto.getPassword().isBlank()) {
                return R.error("密码不可为空");
            }
            //对密码进行MD5加密后替换password
            userMessageDto.setPassword(Md5Utils.hash(userMessageDto.getPassword()));
            UserMessage userMessage = new UserMessage();
            //将userMessageDto中数据拷贝到userMessage
            BeanUtils.copyProperties(userMessageDto, userMessage);
            userMessageMapper.insert(userMessage);
        } catch (Exception e) {
            return R.error(e.toString());
        }
        return R.success("注册成功");
    }
}
