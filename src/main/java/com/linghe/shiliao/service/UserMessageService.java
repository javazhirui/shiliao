package com.linghe.shiliao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linghe.shiliao.common.R;
import com.linghe.shiliao.entity.UserMessage;
import com.linghe.shiliao.entity.dto.LoginDto;
import com.linghe.shiliao.entity.dto.PasswordDto;
import com.linghe.shiliao.entity.dto.UserMessageDto;
import com.linghe.shiliao.utils.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zhao_qin
 * @since 2023-03-14
 */
public interface UserMessageService extends IService<UserMessage> {

    /**
     * 查询用户信息
     *
     * @param userMessageDto
     * @return
     */
    Page<UserMessage> getList(UserMessageDto userMessageDto);

    void editUserMessageBean(UserMessage userMessageDto);

    void delUserMessage(UserMessage userMessage);

    /**
     * 导出用户信息Excel
     *
     * @param split
     * @param excelName
     * @return
     */
    R<String> outputExcelByIds(String[] split, String excelName);

    /**
     * 已知原始密码,修改密码
     *
     * @param request
     * @return
     */
    R<String> updatePassword(HttpServletRequest request, PasswordDto passwordDto);

    /**
     * 添加客户基本信息
     * @param userMessageDto
     * @return
     */
    R<String> addUserBean(UserMessageDto userMessageDto);

    /**
     * 通过姓名查询所有相似名称的客户
     * @param userMessageDto
     * @return
     */
    R<List<UserMessage>> getUserMessages(UserMessageDto userMessageDto);
}
