package com.linghe.shiliao.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Aspect
@Component
public class RuleAopAspect {


    @Before("@annotation(ruleAop)")
    public void doBefore(RuleAop ruleAop) {
        HttpServletRequest request = currentRequest();
        if (Objects.isNull(request)) {
            throw new IllegalArgumentException("请求为空/请求参数错误");
        }
        String[] headerNames = ruleAop.ruleNames();
        for (String headerName : headerNames) {
            String value = request.getHeader(headerName);
            if (StringUtils.hasText(value)) {
                return;
            }
        }
        throw new IllegalArgumentException("没有权限");
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
