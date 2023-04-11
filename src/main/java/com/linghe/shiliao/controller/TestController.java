package com.linghe.shiliao.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linghe.shiliao.common.R;
import com.linghe.shiliao.entity.UserMessage;
import com.linghe.shiliao.entity.dto.UserMessageDto;
import com.linghe.shiliao.mapper.UserMessageMapper;
import com.linghe.shiliao.utils.JwtUtils;
import com.linghe.shiliao.utils.MinIoUtils1;
import com.linghe.shiliao.utils.MinIoUtils2;
import com.linghe.shiliao.utils.WordUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
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
    private MinIoUtils1 minioUtils1;

    @Value("${minio.endpoint}")
    private String address;

    @Value("${minio.bucketName}")
    private String bucketName;

    /**
     * 文件上传测试
//     * @param request 获取id存储对应备用
     * @param file 上传文件
     * @return 返回文件路径
     */
    @ApiOperation("minIO上传文件测试")
    @PostMapping("/uploadFileTest")
    public R<String> uploadFile(/*HttpServletRequest request, */MultipartFile file, UserMessageDto userMessageDto) {
//        String userId = JwtUtils.getUserIdByJwtToken(request);
        List<String> upload = minioUtils1.upload(new MultipartFile[]{file}, userMessageDto);
        String fileUrl = address + "/" + bucketName + "/" + upload.get(0);
        return R.success(fileUrl);
    }

    @Autowired
    private MinIoUtils2 minIoUtils2;

    /**
     * 文件下载测试
     *
     * @param fileName 文件名
     * @param response 返回文件下载信息
     */
    @GetMapping("/download")
    public void download(@RequestParam("fileName") String fileName, HttpServletResponse response) {
        try {
            InputStream fileInputStream = minIoUtils2.getObject(bucketName, fileName);
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setContentType("application/force-download");
            response.setCharacterEncoding("UTF-8");
            IOUtils.copy(fileInputStream, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
