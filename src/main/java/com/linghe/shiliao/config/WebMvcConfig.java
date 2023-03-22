package com.linghe.shiliao.config;

import com.linghe.shiliao.common.JacksonObjectMapper;
import com.linghe.shiliao.filter.JWTInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


@Configuration
class WebMvcConfig implements WebMvcConfigurer {
    //Springboot添加静态资源映射
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/file/**").addResourceLocations("file:D:/");
    }

    /**
     * 扩展mvc框架的消息转换器
     *
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //创建消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器，底层使用Jackson将Java对象转为json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将上面的消息转换器对象追加到mvc框架的转换器集合中
        converters.add(0, messageConverter);
    }


    @Autowired
    private JWTInterceptor jwtInterceptor;

    /**
     * @param registry 拦截器 的 注册器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 把 拦截器 注册到 Mvc, 同时 设置 拦截 和 放过的 信息
        registry.addInterceptor(jwtInterceptor)
                .excludePathPatterns("/user/**","/**/login","/**/register","/file/**");
    }
}
