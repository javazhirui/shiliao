package com.linghe.shiliao.aop;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RuleAop {

    //参数输入为:"权限1","权限2",…………
    public String[] ruleNames();


}
