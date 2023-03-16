package com.linghe.shiliao.controller;


import com.linghe.shiliao.entity.UserMessage;
import com.linghe.shiliao.entity.vo.UserMessageVo;
import com.linghe.shiliao.service.UserMessageService;
import com.linghe.shiliao.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
     * @param userVo
     * @return
     */
    @PostMapping("/getList")
    public Page<UserMessage> getList(@RequestBody UserMessageVo userVo) {
        return userMessageService.getList(userVo);
    }

}

