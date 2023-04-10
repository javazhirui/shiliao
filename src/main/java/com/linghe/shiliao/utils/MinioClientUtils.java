package com.linghe.shiliao.utils;

import com.linghe.shiliao.config.MinioConfig;
import io.minio.*;
import io.minio.messages.Bucket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public class MinioClientUtils {

    private static final Log log = LogFactory.getLog(MinioClientUtils.class);

    private static MinioConfig minioConfig = SpringUtils.getBean(MinioConfig.class);

    /**
     * 内网访问
     */
    private static MinioClient minioClient = SpringUtils.getBean(MinioClient.class);


    /**
     * @Description 创建桶
     *
     * @param bucketName 桶名称
     */

    public static void makeBucket(String bucketName) throws Exception {
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    /**
     * @Description 获取全部bucket
     * <p>
     * https://docs.minio.io/cn/java-client-api-reference.html#listBuckets
     */
    public static List<Bucket> listBuckets() throws Exception {
        return minioClient.listBuckets();
    }

    /**
     * @Description 根据bucketName获取信息
     *
     * @param bucketName bucket名称
     */

    public static Optional<Bucket> getBucket(String bucketName) throws Exception {
        return listBuckets().stream().filter(b -> b.name().equals(bucketName)).findFirst();
    }

    /**
     * @Description 根据bucketName删除信息
     *
     * @param bucketName bucket名称
     */

    public static void removeBucket(String bucketName) throws Exception {
        minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
    }

    /**
     * @Description 获取文件外链
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @return url
     */
    public static String getObjectURL(String bucketName, String objectName) throws Exception {
        return getObjectURL(bucketName, objectName, 7);
    }

    /**
     * @Description 获取文件外链
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param expires    过期时间 <=7
     * @return url
     */
    public static String getObjectURL(String bucketName, String objectName, Integer expires) throws Exception {
        return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().bucket(bucketName).object(objectName).expiry(expires).build());
    }

    /**
     * @Description 获取文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @return 二进制流
     */

    public static InputStream getObject(String bucketName, String objectName) throws Exception {
        return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }

    /**
     * @Description 获取文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @return 二进制流
     */

    public static InputStream getObject(String bucketName, String objectName, long offset, Long length) throws Exception {
        return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).offset(offset).length(length).build());
    }


    /**
     * @Description 获取文件访问地址(有效期)
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @return url
     */
    public static String getPresignedObjectUrl(String bucketName, String objectName) throws Exception {
        return getPresignedObjectUrl(bucketName, objectName, null);
    }

    /**
     * @Description 获取文件访问地址(有效期)
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param expires    过期时间 <=7
     * @return url
     */
    public static String getPresignedObjectUrl(String bucketName, String objectName, Integer expires) throws Exception {
        return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().bucket(bucketName).object(objectName).build());
    }


    /**
     * @Description 删除文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @throws Exception https://docs.minio.io/cn/java-client-api-reference.html#removeObject
     */
    public static void removeObject(String bucketName, String objectName) throws Exception {
        minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }


    public static String putObject(String bucketName, String objectName, MultipartFile file) throws Exception {
        if (objectName== null || "".equals(objectName)) {
            objectName = FileUploadUtils.extractFilename(file);
        }
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build();
        makeBucket(bucketName);
        minioClient.putObject(args);
        return minioConfig.getPreviewUrl() + "/" + bucketName + "/" + objectName;
    }

    /**
     * 编码文件名
     */
    public static final String extractFilename(MultipartFile file)
    {
        String fileName = file.getOriginalFilename();
        String extension = FileUploadUtils.getExtension(file);
        fileName = FileUploadUtils.datePath() + "/" + IdUtils.fastUUID() + "." + extension;
        return fileName;
    }

    public static final String extractFilename(File file)
    {
        String fileName = file.getName();
        String extension = FileUploadUtils.getExtension(file);
        fileName = FileUploadUtils.datePath() + "/" + IdUtils.fastUUID() + "." + extension;
        return fileName;
    }

    public static String putObject(String bucketName, String objectName, File file) throws Exception {
        if (objectName== null || "".equals(objectName)) {
            objectName = FileUploadUtils.extractFilename(file);
        }
        InputStream inputStream = new FileInputStream(file);
        try {
            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(inputStream, file.length(), -1)
                    .contentType(FileUploadUtils.getContentType(file))
                    .build();
            makeBucket(bucketName);
            minioClient.putObject(args);
        } catch (Exception e) {
            throw e;
        } finally {
            inputStream.close();
        }
        return minioConfig.getPreviewUrl() + "/" + bucketName + "/" + objectName;
    }

    public static String putObject(String bucketName, MultipartFile file) throws Exception {
        return putObject(bucketName, null, file);
    }

    public static String putObject(String bucketName, File file) throws Exception {
        return putObject(bucketName, null, file);
    }

}
