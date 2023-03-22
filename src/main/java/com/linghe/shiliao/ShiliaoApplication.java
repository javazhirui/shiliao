package com.linghe.shiliao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.linghe.shiliao.mapper")
@Cacheable //开启缓存
@EnableScheduling //开启定时任务task
public class ShiliaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShiliaoApplication.class, args);
    }

}
