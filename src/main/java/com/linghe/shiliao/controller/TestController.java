package com.linghe.shiliao.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linghe.shiliao.common.R;
import com.linghe.shiliao.entity.UserMessage;
import com.linghe.shiliao.mapper.UserMessageMapper;
import com.linghe.shiliao.utils.MinioUtils;
import com.linghe.shiliao.utils.WordUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api("测试专用控制器")
@RestController
@RequestMapping("/test111")
public class TestController {

    @Autowired
    private UserMessageMapper userMessageMapper;

    @ApiOperation("freemarker导出word测试")
    @GetMapping("/getWordTest")
    public String getWord(@RequestParam String fileName) {
        LambdaQueryWrapper<UserMessage> lqw = new LambdaQueryWrapper<>();
        List<UserMessage> userMessageList = userMessageMapper.selectList(lqw);
        List<Map<String, Object>> list = new ArrayList<>();
        for (UserMessage userMessage : userMessageList) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", userMessage.getName());
            map.put("gender", userMessage.getGender());
            map.put("age", userMessage.getAge());
            map.put("phone", userMessage.getPhone());
            map.put("email", userMessage.getEmail());
            list.add(map);
        }
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("UserList", list);
        WordUtil wordUtil = new WordUtil();
        wordUtil.createWord(dataMap, "ceshi111.xml", fileName);
        return "测试";
    }

    @ApiOperation("xxl-tool导出excel测试")
    @GetMapping("/getExcelTest")
    public String getExcel(@RequestParam String fileName) {
        LambdaQueryWrapper<UserMessage> lqw = new LambdaQueryWrapper<>();
        lqw.orderByDesc(UserMessage::getName);
        List<UserMessage> userMessageList = userMessageMapper.selectList(lqw);
        List<Map<String, Object>> list = new ArrayList<>();
        for (UserMessage userMessage : userMessageList) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", userMessage.getName());
            map.put("gender", userMessage.getGender());
            map.put("age", userMessage.getAge());
            map.put("phone", userMessage.getPhone());
            map.put("email", userMessage.getEmail());
            map.put("health", userMessage.getHealth());
            list.add(map);
        }
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("userList", list);
        WordUtil wordUtil = new WordUtil();
        wordUtil.createWord(dataMap, "用户信息模板.xml", fileName);
        return "测试";
    }

    @Autowired
    private MinioUtils minioUtils;

    @Value("${minio.clientView}")
    private String clientView;

    @Value("${minio.bucketName}")
    private String bucketName;


    @ApiOperation("minIO上传文件测试")
    @PostMapping("/uploadFileTest")
    public R<String> uploadFile(MultipartFile file) {
        List<String> upload = minioUtils.upload(new MultipartFile[]{file});
        return R.success(clientView + "/" + bucketName + "/" + upload.get(0));
    }
}
