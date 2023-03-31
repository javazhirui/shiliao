package com.linghe.shiliao.aop;

import com.linghe.shiliao.common.CustomException;
import com.linghe.shiliao.entity.Loginfo;
import com.linghe.shiliao.service.LoginfoService;
import com.linghe.shiliao.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Aspect
@Component
public class LogAopAspect {

    @Autowired
    private LoginfoService loginfoService;

    @After("@annotation(logAop)")
    public void Before(LogAop logAop) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String thisDateTime = sdf.format(date);
        HttpServletRequest request = currentRequest();
//        备用代码,留存ip
//        String userIp= request.getParameter("ip");
        Loginfo loginfo = new Loginfo();
        loginfo.setCreateTime(thisDateTime);
        loginfo.setUserId(JwtUtils.getUserIdByJwtToken(request));
        loginfo.setLogType(logAop.logType());
        loginfo.setLogMessage(logAop.logMessage());
        //获取所有请求信息
        Map<String, String[]> requestParameterMap = request.getParameterMap();
        List<String> list = new ArrayList<>();
        //或取所有请求头的名称key枚举
        Enumeration<String> headerNames = request.getHeaderNames();
        //循环获取枚举的每一个元素,根据枚举元素获取对应请求头信息
        //最后存入list集合
        List<String> headersList = new ArrayList<>();
        while (headerNames.hasMoreElements()) {
            String hearName = headerNames.nextElement();
            String requestHeader = request.getHeader(hearName);
            headersList.add(hearName + "=" + "[" + requestHeader + "]");
        }
        list.add("requestHeaders" + ": " + headersList.toString());
        //遍历请求信息,转字符串存入集合
        List<String> parametersList = new ArrayList<>();
        requestParameterMap.forEach((key, value) -> {
            List<String> list1 = new ArrayList<>();
            Collections.addAll(list1, value);
            String requestParameter = key + "=" + list1.toString();
            parametersList.add(requestParameter);
        });
        list.add("requestParameters" + ": " + parametersList.toString());
        loginfo.setRequest(list.toString());
        loginfo.setRequestUrl(request.getRequestURI());
        boolean save = loginfoService.save(loginfo);
        if (!save) {
            throw new CustomException("警告!操作日志保存失败!");
        }
    }

    /**
     * 返回请求当前线程绑定，如果没有绑定则返回null
     *
     * @return 返回当前请求
     */
    private HttpServletRequest currentRequest() {
        // Use getRequestAttributes because of its return null if none bound
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        return Optional.ofNullable(servletRequestAttributes)
                .map(ServletRequestAttributes::getRequest).orElse(null);
    }
}
