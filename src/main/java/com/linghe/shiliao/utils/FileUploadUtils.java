package com.linghe.shiliao.utils;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.util.Date;

public class FileUploadUtils {
    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    public static final String extractFilename(File file) {
        String fileName = file.getName();
        String extension = FileUploadUtils.getExtension(file);
        fileName = FileUploadUtils.datePath() + "/" + IdUtils.fastUUID() + "." + extension;
        return fileName;
    }

    /**
     * 编码文件名
     */
    public static final String extractFilename(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String extension = getExtension(file);
        fileName = FileUploadUtils.datePath() + "/" + IdUtils.fastUUID() + "." + extension;
        return fileName;
    }

    public static final String getExtension(File file) {
        String extension = FilenameUtils.getExtension(file.getName());
        if (StringUtils.isEmpty(extension)) {
            extension = MimeTypeUtils.getExtension(FileUploadUtils.getContentType(file));
        }
        return extension;
    }

    public static String getContentType(String filePath) {
        return getContentType(new File(filePath));
    }

    public static String getContentType(File file) {
        String contentType = null;
        try {
            contentType = new MimetypesFileTypeMap().getContentType(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contentType;
    }

    /**
     * 获取文件名的后缀
     *
     * @param file 表单文件
     * @return 后缀名
     */
    public static final String getExtension(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (StringUtils.isEmpty(extension)) {
            extension = MimeTypeUtils.getExtension(file.getContentType());
        }
        return extension;
    }

}
