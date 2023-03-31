package com.linghe.shiliao.aop;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAop {

    /**
     * 操作类型 增/删/改
     *
     * @return
     */
    String logType();

    /**
     * 具体操作内容
     *
     * @return
     */
    String logMessage();

}
