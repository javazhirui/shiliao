package com.linghe.shiliao.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linghe.shiliao.entity.UserMessage;
import com.linghe.shiliao.mapper.UserMessageMapper;
import com.linghe.shiliao.service.UserMessageService;
import com.linghe.shiliao.utils.WordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test111")
public class TestController {

    @Autowired
    private UserMessageMapper userMessageMapper;

    @GetMapping("/getWord")
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

    @GetMapping("/getExcel")
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
            list.add(map);
        }
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("userList", list);
        WordUtil wordUtil = new WordUtil();
        wordUtil.createWord(dataMap, "用户信息模板.ftl", fileName);
        return "测试";
    }
}
