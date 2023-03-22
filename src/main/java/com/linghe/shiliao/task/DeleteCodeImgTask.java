package com.linghe.shiliao.task;

import com.linghe.shiliao.utils.RedisCache;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class DeleteCodeImgTask {

    @Value("${shiliaoFilePath.codeImgPath}")
    private String codeImgPath;


    @Autowired
    private RedisCache redisCache;

    @Scheduled(cron = "0 */5 * * * ?") //每5分钟执行一次
//    @Scheduled(cron="*/5 * * * * ?")
    private void deleteCodeImg() {
        String excelPath = codeImgPath.substring(0,codeImgPath.length()-1);
        File file = new File(excelPath);
        if (!file.exists()) { // 此时文件有父目录
            file.mkdirs(); // 创建父目录
            return;
        }
        List<String> uuidNames = this.getFileNames(codeImgPath);
        for (String uuidName : uuidNames) {
            String[] split = uuidName.split(".jpg");
            String uuid = split[0];
            if (StringUtils.isEmpty(redisCache.getCacheObject(uuid))) {
                File file1 = new File(codeImgPath + uuidName);
                file1.delete();
            }
        }
    }

    /**
     * 得到文件名称
     *
     * @param path 路径
     * @return {@link List}<{@link String}>
     */
    private List<String> getFileNames(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        List<String> fileNames = new ArrayList<>();
        return getFileNames(file, fileNames);
    }

    /**
     * 得到文件名称
     *
     * @param file      文件
     * @param fileNames 文件名
     * @return {@link List}<{@link String}>
     */
    private List<String> getFileNames(File file, List<String> fileNames) {
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                getFileNames(f, fileNames);
            } else {
                fileNames.add(f.getName());
            }
        }
        return fileNames;
    }
}
