package com.linghe.shiliao.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Data
@Component
public class MinIoClientConfig {

    //minio访问url
    @Value("${minio.endpoint}")
    private String endpoint;

    //minio账号
    @Value("${minio.accessKey}")
    private String accessKey;

    //minio密码
    @Value("${minio.secretKey}")
    private String secretKey;


    /**
     * 注入minio 客户端
     *
     * @return
     */
    @Bean
    public MinioClient minioClient() {

        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
}