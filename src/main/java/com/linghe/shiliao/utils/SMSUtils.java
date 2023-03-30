package com.linghe.shiliao.utils;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

/**
 * 短信发送工具类
 */
@Slf4j
public class SMSUtils {

    /**
     * 发送短信
     *
     * @param signName     签名
     * @param templateCode 模板
     * @param phoneNumbers 手机号
     * @param code        参数
     */
    public static void sendMessage(String signName, String templateCode, String phoneNumbers, String code) {
        // 连接阿里云
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI5tPwY25HUq3fS7tMpj1U", "RI6TQPzyKCa43f6GiCb7k52bgWgBkn");
        IAcsClient client = new DefaultAcsClient(profile);
        // 构建请求
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        // 这些内容不要动，是人家阿里爸爸弄出来的，咱不用管
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");

        // 自己的内容,此处 SendSms 为发送验证码
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");

        //自定义的参数(手机号，模板ID，签名 )
        request.putQueryParameter("PhoneNumbers", phoneNumbers);
        request.putQueryParameter("TemplateCode", templateCode);
        request.putQueryParameter("SignName", signName);
        //构建一个短信的验证码
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");

        try {
            CommonResponse response = client.getCommonResponse(request);
            log.info(response.getData());
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}


