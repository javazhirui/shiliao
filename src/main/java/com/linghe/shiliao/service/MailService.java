package com.linghe.shiliao.service;

import com.linghe.shiliao.common.R;

import java.util.List;


public interface MailService {



    /** 1
     * @description 发送简单文本邮件
     * @param mailRecipient  邮件接收方
     * @param subject  邮件主题
     * @param messageText  HTML格式的邮件文本内容
     */
    void sendSimpleMail(String mailRecipient, String subject , String messageText);

    /** 2
     * @description 发送HTML格式邮件
     * @param mailRecipient 邮件接收方
     * @param subject 邮件主题
     * @param htmlText HTML格式的邮件文本内容
     */
    void sendHtmlMail(String mailRecipient, String subject , String htmlText);


    /** 3
     * @description 发送包含附件的邮件
     * @param mailRecipient 邮件接收方
     * @param subject 邮件主题
     * @param messageText 邮件文本内容
     * @param filePathList 附件（文件路径集合）
     */
    void sendAppendixMail(String mailRecipient , String subject , String messageText , List<String> filePathList);

    /**
     * 获取邮箱验证码
     * @param uuid
     * @param email
     * @param code
     * @return
     */
    R<String> getEmailCode(String uuid, String email, String code);
}
