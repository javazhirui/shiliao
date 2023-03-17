package com.linghe.shiliao.controller;


import com.linghe.shiliao.entity.UserMessage;
import com.linghe.shiliao.entity.dto.UserMessageDto;
import com.linghe.shiliao.service.UserMessageService;
import com.linghe.shiliao.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhao_qin
 * @since 2023-03-14
 */
@RestController
@RequestMapping("/userMessage")
public class UserMessageController {

    @Autowired
    private UserMessageService userMessageService;

    /**
     * 查询用户信息
     * @param userMessageDto
     * @return
     */
    @PostMapping("/getList")
    public Page<UserMessage> getList(@RequestBody UserMessageDto userMessageDto) {
        return userMessageService.getList(userMessageDto);
    }

    @PostMapping("/editUserMessageBean")
    public void editUserMessageBean(@RequestBody UserMessage userMessage){
        userMessageService.editUserMessageBean(userMessage);
    }

    @PostMapping("/delUserMessage")
    public void delUserMessage(@RequestBody UserMessage userMessage){
        userMessageService.delUserMessage(userMessage);
    }

}

