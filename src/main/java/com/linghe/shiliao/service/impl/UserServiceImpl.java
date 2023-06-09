package com.linghe.shiliao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linghe.shiliao.common.R;
import com.linghe.shiliao.entity.UserMessage;
import com.linghe.shiliao.entity.dto.LoginDto;
import com.linghe.shiliao.entity.dto.UserMessageDto;
import com.linghe.shiliao.mapper.UserMessageMapper;
import com.linghe.shiliao.service.UserService;
import com.linghe.shiliao.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMessageMapper userMessageMapper;

    //操作Redis工具类
    @Autowired
    private RedisCache redisCache;


    //验证码生成路径
    @Value("${shiliaoFilePath.codeImgPath}")
    private String codeImgPath;

    //图片验证码有效时长(秒)
    @Value("${shiliaoRedisTime.codeTime}")
    private String codeTime;

    @Value("${shiliaoRedisTime.phoneCodeTime}")
    private String phoneCodeTime;

    @Value("${jwtDeadTime}")
    private String jwtDeadTime;


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
            return R.error("页面uuid不可为空");
        }
        long thisTime = System.currentTimeMillis();
        Long getTime = redisCache.getCacheObject(uuid + "time");
        if (!ObjectUtils.isEmpty(getTime)) {
            if (thisTime - getTime < 1000) {
                return R.error("验证码获取太过频繁");
            }
        }

        String code = VerifyCodeUtils.generateVerifyCode(6);
        String codeRedis = Md5Utils.hash(code);
        //code缓存到Redis
        Long l = System.currentTimeMillis();
        redisCache.setCacheObject(uuid + "time", l, Integer.parseInt(codeTime), TimeUnit.SECONDS);
        redisCache.setCacheObject(uuid, codeRedis, Integer.parseInt(codeTime), TimeUnit.SECONDS);


        String CodeImgPath = codeImgPath + uuid + ".jpg";//后期可换minio地址
        File file = new File(CodeImgPath);
        if (!file.getParentFile().exists()) { // 此时文件有父目录
            file.getParentFile().mkdirs(); // 创建父目录
        }
        //构造输出流
        OutputStream os = new FileOutputStream(file);
        //生成验证码图片  参数(验证码长,高,字节输出流,验证码)
        VerifyCodeUtils.outputImage(600, 150, os, code);
        os.close();

        return R.success(uuid + ".jpg");
    }

    /**
     * 登录方法
     *
     * @param loginDto 账号,密码,验证码,
     * @return 返回一个token
     */
    @Override
    public R<String> login(HttpServletResponse response, LoginDto loginDto) {
        String code = Md5Utils.hash(loginDto.getCode());
        String codeRedis = redisCache.getCacheObject(loginDto.getUuid());
        if (!StringUtils.equals(code, codeRedis)) {
            return R.error("验证码输入错误");
        }
        LambdaQueryWrapper<UserMessage> lqw = new LambdaQueryWrapper<>();
        lqw.eq(UserMessage::getEmail, loginDto.getUserName());
        lqw.eq(UserMessage::getPassword, Md5Utils.hash(loginDto.getPassword()));
        UserMessage userMessage = userMessageMapper.selectOne(lqw);
        if (ObjectUtils.isEmpty(userMessage)) {
            LambdaQueryWrapper<UserMessage> lqw1 = new LambdaQueryWrapper<>();
            lqw1.eq(UserMessage::getUserName, loginDto.getUserName());
            lqw1.eq(UserMessage::getPassword, Md5Utils.hash(loginDto.getPassword()));
            UserMessage userMessage1 = userMessageMapper.selectOne(lqw1);
            if (ObjectUtils.isEmpty(userMessage1)) {
                return R.error("账号或密码错误");
            }
            if (userMessage1.getStatus() != 1) {
                return R.error("账号已停用,请联系管理员");
            }
            userMessage = userMessage1;
        }
        if (userMessage.getStatus() != 1) {
            return R.error("账号已停用,请联系管理员");
        }
        String token = JwtUtils.getJwtToken(userMessage.getUserId().toString(), userMessage.getRuleId().toString(), loginDto.getUuid());
        response.setHeader("token", token);
        int i = Integer.parseInt(jwtDeadTime);
        redisCache.setCacheObject("token_" + userMessage.getUserId(), token, i / 2, TimeUnit.SECONDS);
        redisCache.setCacheObject("login_" + userMessage.getUserId(), loginDto.getUuid(), i, TimeUnit.SECONDS);
        Integer ruleId = userMessage.getRuleId();
        response.setHeader("ruleId", ruleId.toString());
        return R.success("登陆成功");
    }

    /**
     * 邮箱注册方法
     *
     * @param userMessageDto UUID 账号密码 email 邮箱验证码 验证码  姓名 联系电话
     * @return
     */
    @Override
    public R<String> register(UserMessageDto userMessageDto) {

        if (ObjectUtils.isEmpty(userMessageDto)) {
            return R.error("数据为空,请检查");
        }
        if (userMessageDto.getEmail().isEmpty()) {
            return R.error("邮箱为空");
        }

        if (StringUtils.isBlank(userMessageDto.getCode())) {
            return R.error("验证码为空,请检查");
        }

        //查询构造器
        LambdaQueryWrapper<UserMessage> lqw = new LambdaQueryWrapper<>();
        //查询email
        lqw.eq(UserMessage::getEmail, userMessageDto.getEmail());
        //获取第一个查询结果
        UserMessage userMessage = userMessageMapper.selectOne(lqw);
        if (!ObjectUtils.isEmpty(userMessage)) {
            return R.error("邮箱已注册,请更换邮箱或重新登陆");
        }
        LambdaQueryWrapper<UserMessage> lqw1 = new LambdaQueryWrapper<>();
        lqw1.eq(UserMessage::getUserName, userMessageDto.getUserName());
        UserMessage userMessage1 = userMessageMapper.selectOne(lqw1);
        if (!ObjectUtils.isEmpty(userMessage1)) {
            return R.error("用户名(邮箱)已存在");
        }
        LambdaQueryWrapper<UserMessage> lqw2 = new LambdaQueryWrapper<>();
        lqw2.eq(UserMessage::getPhone, userMessageDto.getUserName());
        UserMessage userMessage2 = userMessageMapper.selectOne(lqw2);
        if (!ObjectUtils.isEmpty(userMessage2)) {
            return R.error("用户名(手机号)已存在");
        }
        //根据页面唯一uuId获取缓存code(md5加密后)
        String codeRedis = redisCache.getCacheObject(userMessageDto.getUuid());
        //前端用户输入的验证码
        String code = userMessageDto.getCode();
        code = Md5Utils.hash(code);
        if (!StringUtils.equals(code, codeRedis)) {
            return R.error("验证码输入有误,请重新输入");
        }

        //开启手机验证码功能
/*        String phoneCode = userMessageDto.getPhoneCode();
        if (phoneCode.isEmpty()) {
            return R.error("手机验证码为空,请检查");
        }
        phoneCode = Md5Utils.hash(phoneCode);
        String phoneCodeRedis = redisCache.getCacheObject(userMessageDto.getPhoneCode() + "_" + userMessageDto.getUuid());
        if (!StringUtils.equals(phoneCode,phoneCodeRedis)) {
            return R.error("手机验证码输入有误");
        }*/


        String emailCode = userMessageDto.getEmailCode();
        if (emailCode.isEmpty()) {
            return R.error("邮箱验证码为空,请检查");
        }
        emailCode = Md5Utils.hash(emailCode);
        //获取缓存的邮箱验证码(md5加密后的)
        String emailCodeRedis = redisCache.getCacheObject(userMessageDto.getEmail() + "_" + userMessageDto.getUuid());
        if (!StringUtils.equals(Md5Utils.hash(emailCode), emailCodeRedis)) {
            return R.error("邮箱验证码输入有误");
        }
        if (userMessageDto.getPassword().isEmpty()) {
            return R.error("密码不可为空");
        }
        //对密码进行MD5加密后替换password
        userMessageDto.setPassword(Md5Utils.hash(userMessageDto.getPassword()));
        UserMessage user = new UserMessage();
        //将userMessageDto中数据拷贝到user
        BeanUtils.copyProperties(userMessageDto, user);
        userMessageMapper.insert(user);

        return R.success("注册成功");
    }

    /**
     * 忘记密码,修改密码
     *
     * @param userMessageDto password,uuid,code,email,emailCode
     * @return
     */
    @Override
    public R<String> forgetPassword(UserMessageDto userMessageDto) {

        if (ObjectUtils.isEmpty(userMessageDto)) {
            return R.error("输入信息为空");
        }

        String codeRedis = redisCache.getCacheObject(userMessageDto.getUuid());
        if (!StringUtils.equals(Md5Utils.hash(userMessageDto.getCode()), codeRedis)) {
            return R.error("验证码输入有误,请重新输入");
        }

        if (userMessageDto.getEmail().isEmpty()) {
            return R.error("邮箱为空");
        }

        LambdaQueryWrapper<UserMessage> lqw = new LambdaQueryWrapper<>();
        lqw.eq(UserMessage::getEmail, userMessageDto.getEmail());
        UserMessage userMessage = userMessageMapper.selectOne(lqw);
        if (ObjectUtils.isEmpty(userMessage)) {
            return R.error("邮箱不存在,请先注册");
        }


        String emailCode = userMessageDto.getEmailCode();
        String emailCodeRedis = redisCache.getCacheObject(userMessageDto.getEmail() + "_" + userMessageDto.getUuid());
        if (!StringUtils.equals(Md5Utils.hash(emailCode), emailCodeRedis)) {
            return R.error("邮箱验证码输入有误");
        }

        if (userMessageDto.getPassword().isEmpty()) {
            return R.error("输入密码为空");
        }


        if (StringUtils.equals(Md5Utils.hash(userMessageDto.getPassword()), userMessage.getPassword())) {
            return R.error("新密码不可与原密码相同");
        }
        userMessage.setPassword(Md5Utils.hash(userMessageDto.getPassword()));
        int i = userMessageMapper.updateById(userMessage);
        if (i < 1) {
            return R.error("修改密码意外失败");
        }

        return R.success("密码修改成功");
    }

    @Override
    public R<String> getCode1(String uuid) throws IOException {
        if (StringUtils.isBlank(uuid)) {
            return R.error("页面uuid不可为空");
        }
        long thisTime = System.currentTimeMillis();
        Long getTime = redisCache.getCacheObject(uuid + "time");
        if (!ObjectUtils.isEmpty(getTime)) {
            if (thisTime - getTime < 1000) {
                return R.error("验证码获取太过频繁");
            }
        }

        String code = VerifyCodeUtils.generateVerifyCode(6);
        String codeRedis = Md5Utils.hash(code);
        //code缓存到Redis
        Long l = System.currentTimeMillis();
        redisCache.setCacheObject(uuid + "time", l, Integer.parseInt(codeTime), TimeUnit.SECONDS);
        redisCache.setCacheObject(uuid, codeRedis, Integer.parseInt(codeTime), TimeUnit.SECONDS);


        String CodeImgPath = codeImgPath + uuid + ".jpg";
        File file = new File(CodeImgPath);
        if (!file.getParentFile().exists()) { // 此时文件有父目录
            file.getParentFile().mkdirs(); // 创建父目录
        }
        //构造输出流
        OutputStream os = new FileOutputStream(file);
        //生成验证码图片  参数(验证码长,高,字节输出流,验证码)
        VerifyCodeUtils.outputImage(600, 150, os, code);
        os.close();

        return R.success("验证码已生成");
    }

    @Override
    public R<String> getCode64(String uuid) throws IOException {
        if (StringUtils.isBlank(uuid)) {
            return R.error("页面uuid不可为空");
        }
        long thisTime = System.currentTimeMillis();
        Long getTime = redisCache.getCacheObject(uuid + "time");
        if (!ObjectUtils.isEmpty(getTime)) {
            if (thisTime - getTime < 1000) {
                return R.error("验证码获取太过频繁");
            }
        }

        String code = VerifyCodeUtils.generateVerifyCode(6);
        String codeRedis = Md5Utils.hash(code);
        //code缓存到Redis
        Long l = System.currentTimeMillis();
        redisCache.setCacheObject(uuid + "time", l, Integer.parseInt(codeTime), TimeUnit.SECONDS);
        redisCache.setCacheObject(uuid, codeRedis, Integer.parseInt(codeTime), TimeUnit.SECONDS);

        //构造输出流
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        //生成验证码图片  参数(验证码长,高,字节输出流,验证码)
        VerifyCodeUtils.outputImage(600, 150, os, code);

        return R.success("验证码在map里").add("img", Base64.encode(os.toByteArray()));
    }

    @Override
    public R<String> getPhoneCode(String uuid, String phone) {
        String s = VerifyCodeUtils.generateVerifyCode(6);
        try {
            SMSUtils.sendMessage("阿里云短信测试", "SMS_154950909", phone, s);
        } catch (Exception e) {
            log.info(e.getMessage());
            R.error("获取验证码意外失败");
        }
        redisCache.setCacheObject(phone + "_" + uuid, Md5Utils.hash(s), Integer.parseInt(phoneCodeTime), TimeUnit.SECONDS);
        redisCache.setCacheObject(phone + "_" + uuid + "time", System.currentTimeMillis(), Integer.parseInt(phoneCodeTime), TimeUnit.SECONDS);
        return R.success("短信验证码发送成功");
    }


}
