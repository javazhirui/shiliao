package com.linghe.shiliao.service;

import com.linghe.shiliao.entity.OssFile;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传接口
 */
public interface IFileService {
    /**
     * 文件上传接口
     *
     * @param file    上传的文件
     * @param ossFile 文件业务数据
     * @return 访问地址
     * @throws Exception
     */
    OssFile uploadFile(MultipartFile file, OssFile ossFile) throws Exception;

}
