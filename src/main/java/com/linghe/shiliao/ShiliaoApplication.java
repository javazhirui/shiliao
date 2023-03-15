package com.linghe.shiliao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.linghe.shiliao.mapper")
public class ShiliaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShiliaoApplication.class, args);
    }

}
