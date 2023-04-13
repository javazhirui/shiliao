package com.linghe.shiliao.aop;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAop {

    /**
     * 操作类型 增/删/改/查/上传/下载
     */
    String logType();

    /**
     * 具体操作内容
     */
    String logMessage();

}
