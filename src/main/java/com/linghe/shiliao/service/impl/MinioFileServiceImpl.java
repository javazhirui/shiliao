package com.linghe.shiliao.service.impl;

import com.linghe.shiliao.common.R;
import com.linghe.shiliao.config.MinioConfig;
import com.linghe.shiliao.entity.OssFile;
import com.linghe.shiliao.service.IFileService;
import com.linghe.shiliao.utils.FileUploadUtils;
import com.linghe.shiliao.utils.MinioClientUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Minio 文件存储
 */
@Primary
@Service
public class MinioFileServiceImpl implements IFileService {

    @Autowired
    private MinioConfig minioConfig;

    /**
     * 文件上传接口
     *
     * @param file    上传的文件
     * @param ossFile 文件业务数据
     * @return 访问地址
     */
    @Override
    public R<OssFile> uploadFile(MultipartFile file, OssFile ossFile) throws Exception {
        String objectName = FileUploadUtils.extractFilename(file);
        String bucketName = minioConfig.getBucketName();
        String url = MinioClientUtils.putObject(bucketName, objectName, file);
        OssFile valueFile = this.getOssFile(file, ossFile);
        valueFile.setUrl(url);
        valueFile.setFileKey(objectName);
        valueFile.setFileDir(bucketName);
        return R.success(valueFile);
    }


    private OssFile getOssFile(MultipartFile file, OssFile ossFile) {
        // 上传并返回新文件名称（FileKey）
        OssFile valueFile = new OssFile();
        // 数据库存储文件信息
        valueFile.setFileName(file.getOriginalFilename());
        valueFile.setFileLength(file.getSize());
        valueFile.setFileExtension(FileUploadUtils.getExtension(file));
        if (StringUtils.isNotEmpty(ossFile.getBusinessId().toString())) {
            valueFile.setBusinessId(ossFile.getBusinessId());
        }
        valueFile.setBusinessNo(ossFile.getBusinessNo());
        return valueFile;
    }

}
