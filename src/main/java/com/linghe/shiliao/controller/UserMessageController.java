package com.linghe.shiliao.controller;


import com.linghe.shiliao.common.R;
import com.linghe.shiliao.entity.UserMessage;
import com.linghe.shiliao.entity.dto.LoginDto;
import com.linghe.shiliao.entity.dto.PasswordDto;
import com.linghe.shiliao.entity.dto.UserMessageDto;
import com.linghe.shiliao.service.UserMessageService;
import com.linghe.shiliao.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    /**
     * 导出用户信息Excel
     * @param excel
     * @param excelName
     * @return
     */
    @GetMapping("/outputExcelByIds")
    public R<String> outputExcelByIds(@RequestParam String excel, @RequestParam String excelName) {
        System.err.println(excel);
        String[] ids = excel.split(",");
        return userMessageService.outputExcelByIds(ids,excelName);
    }

    /**
     * 已知原始密码修改密码接口
     * @param request
     * @return
     */
    @PostMapping("/updatePassword")
    public R<String> updatePassword(HttpServletRequest request, PasswordDto passwordDto) {
        return userMessageService.updatePassword(request,passwordDto);
    }

    /**
     * 普通用户登录
     * @param loginDto
     * @return
     */
    @PostMapping("/getUserBean")
    public UserMessage getUserBean(@RequestBody LoginDto loginDto){
        return  userMessageService.getUserBean(loginDto);
    }



}

