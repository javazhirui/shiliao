package com.linghe.shiliao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@MapperScan("com.linghe.shiliao.mapper")
@Cacheable        //开启缓存
@EnableScheduling //开启定时任务task
@EnableOpenApi    //开启swagger3.0
public class ShiliaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShiliaoApplication.class, args);
    }

}
