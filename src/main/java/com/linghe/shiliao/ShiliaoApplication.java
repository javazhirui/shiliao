package com.linghe.shiliao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.Cacheable;

@SpringBootApplication
@MapperScan("com.linghe.shiliao.mapper")
@Cacheable //开启缓存
public class ShiliaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShiliaoApplication.class, args);
    }

}
