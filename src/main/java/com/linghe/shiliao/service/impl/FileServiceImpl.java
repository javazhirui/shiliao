package com.linghe.shiliao.service.impl;


import com.linghe.shiliao.common.R;
import com.linghe.shiliao.config.MinioConfig;
import com.linghe.shiliao.service.FileService;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class FileServiceImpl extends FileService {

    @Autowired
    MinioConfig minioConfig;

    /**
     * 文件上传至Minio
     * 使用try catch finally进行上传
     * finally里进行资源的回收
     */
    @Override
    public R<String> upload(File file) {
        InputStream inputStream = null;
        //创建Minio的连接对象
        MinioClient minioClient = getClient();
        String bucketName = minioConfig.getBucketName();
        try {
            inputStream = new FileInputStream(file);
            //基于官网的内容，判断文件存储的桶是否存在 如果桶不存在就创建桶
            Boolean b = bucketExists(bucketName, minioClient);
            if (!b) {
                makeBucket(bucketName, minioClient);
            }
            String filename = file.getName();
            if (StringUtils.isBlank(filename)) {
                throw new RuntimeException();
            }

            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            String contentType = filename.substring(filename.lastIndexOf("."));
            filename = df.format(new Date()) + "/" + UUID.randomUUID() + filename.substring(filename.lastIndexOf("."));
            PutObjectArgs objectArgs = PutObjectArgs.builder().bucket(minioConfig.getBucketName())
                    .object(filename).stream(inputStream, file.length(), -1)
                    .contentType(contentType).build();
            minioClient.putObject(objectArgs);
            /**
             * 构建返回结果集
             */
            Map<String,String > map = new HashMap<>();
            String s = "http://192.168.0.104:9000/" + bucketName + "/" + filename;
            return R.success(s);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("上传失败");
        } finally {
            //防止内存泄漏
            if (inputStream != null) {
                try {
                    inputStream.close(); // 关闭流
                } catch (IOException e) {
                    log.debug("inputStream close IOException:" + e.getMessage());
                }
            }
        }
    }


    /**
     * 免费提供一个获取Minio连接的方法
     * 获取Minio连接
     *
     * @return
     */
    private MinioClient getClient() {
        MinioClient minioClient =
                MinioClient.builder()
                        .endpoint("http://" + minioConfig.getEndpoint() + ":" + minioConfig.getPort())
                        .credentials(minioConfig.getAccessKey(), minioConfig.getSecretKey())
                        .build();
        return minioClient;
    }

    /**
     * 查看存储bucket是否存在
     *
     * @param bucketName  桶名
     * @param minioClient minio的连接对象
     * @return boolean
     */
    public Boolean bucketExists(String bucketName, MinioClient minioClient) {
        Boolean found;
        try {
            found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return found;
    }

    /**
     * 创建存储bucket
     *
     * @param bucketName  桶名
     * @param minioClient minio的连接对象
     * @return boolean
     */
    public Boolean makeBucket(String bucketName, MinioClient minioClient) {
        try {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
