package com.linghe.shiliao.controller;


import com.linghe.shiliao.common.R;
import com.linghe.shiliao.entity.UserMessage;
import com.linghe.shiliao.entity.dto.LoginDto;
import com.linghe.shiliao.entity.dto.PasswordDto;
import com.linghe.shiliao.entity.dto.UserMessageDto;
import com.linghe.shiliao.service.UserMessageService;
import com.linghe.shiliao.utils.JwtUtils;
import com.linghe.shiliao.utils.Page;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
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
@Api("用户基本信息控制器")
@RestController
@RequestMapping("/userMessage")
public class UserMessageController {

    @Autowired
    private UserMessageService userMessageService;

    /**
     * 查询用户信息
     *
     * @param userMessageDto
     * @return
     */
    @ApiOperation("查询用户信息")
    @PostMapping("/getList")
    public Page<UserMessage> getList(@RequestBody UserMessageDto userMessageDto) {
        return userMessageService.getList(userMessageDto);
    }

    /**
     * 修改用户个人信息
     * @param userMessage
     */
    @PostMapping("/editUserMessageBean")
    public void editUserMessageBean(@RequestBody  UserMessage userMessage) {
        userMessageService.editUserMessageBean(userMessage);
    }

    /**
     * 删除/隐藏用户个人信息
     * @param userMessage
     */
    @ApiOperation("删除/隐藏用户个人信息")
    @PostMapping("/delUserMessage")
    public void delUserMessage(@RequestBody UserMessage userMessage) {
        userMessageService.delUserMessage(userMessage);
    }

    /**
     * 导出用户信息Excel
     *
     * @param excel
     * @param excelName
     * @return
     */
    @ApiOperation("导出用户信息Excel")
    @GetMapping("/outputExcelByIds")
    public R<String> outputExcelByIds(@RequestParam String excel, @RequestParam String excelName) {
        System.err.println(excel);
        String[] ids = excel.split(",");
        return userMessageService.outputExcelByIds(ids, excelName);
    }

    /**
     * 登录状态下,已知原始密码修改密码接口
     *
     * @param request
     * @return
     */
    @ApiOperation("登录状态下,已知原始密码修改密码接口")
    @PostMapping("/updatePassword")
    public R<String> updatePassword(HttpServletRequest request, PasswordDto passwordDto) {
        return userMessageService.updatePassword(request, passwordDto);
    }


    /**
     *
     *
     * @param request
     * @return
     */
    @ApiOperation("获取登录用户自己的信息")
    @GetMapping("/getMyById")
    public R<UserMessage> getById(HttpServletRequest request) {
        UserMessage userMessage = userMessageService.getById(JwtUtils.getUserIdByJwtToken(request));
        if (ObjectUtils.isEmpty(userMessage)) {
            return R.error("获取个人信息失败");
        }
        return R.success(userMessage);
    }
//
//    /**
//     * 普通用户登录查询
//     * @param loginDto
//     * @return
//     */
//    @PostMapping("/getUserBean")
//    public UserMessage getUserBean(@RequestBody LoginDto loginDto){
//        return userMessageService.getUserBean(loginDto);
//    }

    /**
     * 普通用户登录
     * @param loginDto
     * @return
     */
    @ApiOperation("新增用户信息,管理员新增用户使用")
    @PostMapping("/addUserMessageBean")
    public R<String> addUserMessageBean(@RequestBody UserMessageDto userMessageDto){
        if(ObjectUtils.isEmpty(userMessageDto)){
            return R.error("用户信息为空");
        }
        return userMessageService.addUserBean(userMessageDto);
    }

    /**
     * 通过姓名查询所有相似名称的客户
     * @param userMessageDto
     * @return
     */
    @PostMapping("/getUserMessages")
    public R<List<UserMessage>> getUserMessages(@RequestBody UserMessageDto userMessageDto){
        System.err.println(userMessageDto.getName());
        if(null == userMessageDto.getName() || userMessageDto.getName().equals("")){
            return R.error("请输入所需要添加的用户名称");
        }
        return userMessageService.getUserMessages(userMessageDto);
    }



}

