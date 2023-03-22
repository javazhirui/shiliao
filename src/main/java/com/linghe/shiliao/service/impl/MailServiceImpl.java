package com.linghe.shiliao.service.impl;


import com.linghe.shiliao.common.R;
import com.linghe.shiliao.service.MailService;
import com.linghe.shiliao.utils.Md5Utils;
import com.linghe.shiliao.utils.RedisCache;
import com.linghe.shiliao.utils.VerifyCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class MailServiceImpl implements MailService {


    // JavaMailSender是一个用于发送Java邮件的框架，用于简化基于Java的邮件发送。
    // 它封装了JavaMail API，允许开发者使用简单的API来发送和接收电子邮件。
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private RedisCache redisCache;


    //邮件发送方邮箱
    @Value("${spring.mail.username}")
    private String MailSender;

    //邮箱验证码有效时长(秒)
    @Value("{shiliaoRedisTime.emailCodeTime}")
    private String emailCodeTime;

    /**
     * @param mailRecipient 邮件接收方
     * @param subject       邮件主题
     * @param messageText   邮件文本内容
     * @description 发送简单文件邮件
     */
    @Override
    public void sendSimpleMail(String mailRecipient, String subject, String messageText) {

        //1、创建邮件对象（设置参数后提交）
        SimpleMailMessage message = new SimpleMailMessage();
        //2、设置主题
        message.setSubject(subject);
        //3、设置邮件发送者
        message.setFrom(MailSender);
        //4、设置邮件接受者，多个接受者传参为数组格式
        message.setTo(mailRecipient);
        //5、设置邮件正文（邮件的正式内容）
        message.setText(messageText);
        //6、发送邮件
        try {
            javaMailSender.send(message);
            log.info("文本邮件已发送成功");
        } catch (MailException e) {
            e.printStackTrace();
            throw new RuntimeException("邮件发送异常");
        }
    }

    /**
     * @param mailRecipient 邮件接收方
     * @param subject       邮件主题
     * @param htmlText      HTML格式的邮件内容
     * @description 发送HTML格式文件邮件
     */
    @Override
    public void sendHtmlMail(String mailRecipient, String subject, String htmlText) {

        //1、获取MimeMessage对象，多用途的网际邮件扩充协议
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            //2、设置邮件发送者
            mimeMessageHelper.setFrom(MailSender);
            //3、设置邮件接受方
            mimeMessageHelper.setTo(mailRecipient);
            //4、设置邮件主题
            mimeMessageHelper.setSubject(subject);
            //5、设置邮件内容（HTML格式邮件内容），带html格式第二个参数true
            mimeMessageHelper.setText(htmlText, true);
            //6、发送邮件
            javaMailSender.send(mimeMessage);
            log.info("html格式的邮件发送成功");

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("邮件发送异常");
        }

    }

    /**
     * 发送带有附件的邮件
     *
     * @param mailRecipient 邮件接收方
     * @param subject       邮件主题
     * @param messageText   邮件文本内容
     * @param filePathList  附件（文件路径集合）
     */
    @Override
    public void sendAppendixMail(String mailRecipient, String subject, String messageText, List<String> filePathList) {

        //1、获取MimeMessage对象，多用途的网际邮件扩充协议
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            //2、设置邮件发送方
            mimeMessageHelper.setFrom(MailSender);
            //3、设置邮件接收方
            mimeMessageHelper.setTo(mailRecipient);
            //4、设置邮件主题
            mimeMessageHelper.setSubject(subject);
            //5、设置邮件内容（第二个参数需要为true）
            mimeMessageHelper.setText(messageText, true);
            //附件内容
            for (String filePath : filePathList) {
                //FileSystemResource是Spring框架中的一个类,用来以URL或者File方式访问文件系统中的文件
                //通过FileSystemResource对象以File方式访问指定文件
                // public FileSystemResource(File file): filePath表示要访问的文件路径
                FileSystemResource file = new FileSystemResource(new File(filePath));

                //从给定的文件路径中提取出文件的文件名
//                FileSystemResource file = new FileSystemResource(new File("文件地址"));
//                String filename = file.getFilename(); // 获取附件名称
                // 例如：如果输入的文件路径“C:\folder\file.txt”，那么它将提取出文件名为“file.txt”。
                String fileName = file.getFilename();
                log.info("附件名称:" + fileName);
                //addAttachment()方法:添加附件到电子邮件中。它接收两个参数，
                // 第一个参数:文件的文件名，
                // 第二个参数:文件对象
                // 结合使用这两个参数，可以将一个或多个文件添加到电子邮件中
                mimeMessageHelper.addAttachment(fileName, file);
            }
            //6、发送文件
            javaMailSender.send(mimeMessage);
            log.info("带有附件的邮件发送成功");

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("邮件发送异常");
        }
    }

    /**
     * 获取邮箱验证码
     *
     * @param uuid
     * @param email
     * @return
     */
    @Override
    public R<String> getEmailCode(String uuid, String email, String code) {
        if (uuid.isEmpty()) {
            return R.error("uuId为空");
        }
        if (email.isEmpty()) {
            return R.error("邮箱为空");
        }
        if (code.isEmpty()) {
            return R.error("验证码为空");
        }
        String codeRides = redisCache.getCacheObject(uuid);
        code = Md5Utils.hash(code);
        if (!StringUtils.equals(code,codeRides)) {
            return R.error("图片验证码输入有误");
        }
        String emailCode = VerifyCodeUtils.generateVerifyCode(6);
        String emailMessage = "您的验证码为:" + emailCode + ",有效期5分钟,如非本人操作,请勿泄露!";
        this.sendSimpleMail(email, "您的食疗小助手", emailMessage);
        emailCode = Md5Utils.hash(emailCode);
        String emailKey = email + "_" + uuid;
        redisCache.setCacheObject(emailKey, emailCode, Integer.parseInt(emailCodeTime), TimeUnit.SECONDS);
        return R.success("邮箱验证已发送");
    }
}