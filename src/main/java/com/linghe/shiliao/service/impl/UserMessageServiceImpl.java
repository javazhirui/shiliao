package com.linghe.shiliao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linghe.shiliao.common.R;
import com.linghe.shiliao.entity.UserMessage;
import com.linghe.shiliao.entity.dto.UserMessageDto;
import com.linghe.shiliao.mapper.CasesMapper;
import com.linghe.shiliao.mapper.UserMessageMapper;
import com.linghe.shiliao.service.UserMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linghe.shiliao.task.LocalFileTask;
import com.linghe.shiliao.utils.JwtUtils;
import com.linghe.shiliao.utils.Page;
import com.xxl.tool.excel.ExcelTool;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhao_qin
 * @since 2023-03-14
 */
@Service
public class UserMessageServiceImpl extends ServiceImpl<UserMessageMapper, UserMessage> implements UserMessageService {

    @Autowired
    private UserMessageMapper userMessageMapper;

    @Autowired
    private LocalFileTask localFileTask;

    @Value("${shiliaoFilePath.userMessageExcelPath}")
    private String userMessageExcel;


    /**
     * 查询用户信息
     *
     * @param userMessageDto
     * @return
     */
    @Override
    public Page<UserMessage> getList(UserMessageDto userMessageDto) {
        Page<UserMessage> page = new Page<>();
        page.setCurrentPage(userMessageDto.getCurrentPage());
        page.setPageSize(userMessageDto.getPageSize());
        userMessageDto.setCurrentPage((userMessageDto.getCurrentPage() - 1) * userMessageDto.getPageSize());
        page.setTotal(userMessageMapper.getTotal(userMessageDto));
        page.setList(userMessageMapper.getList(userMessageDto));
        return page;
    }

    @Override
    public void editUserMessageBean(UserMessage userMessage) {
        UserMessage userMessage2 = new UserMessage();
        BeanUtils.copyProperties(userMessage, userMessage2);
        this.updateById(userMessage2);
    }

    @Override
    public void delUserMessage(UserMessage userMessage) {
        UserMessage userMessage2 = new UserMessage();
        BeanUtils.copyProperties(userMessage, userMessage2);
        this.updateById(userMessage2);
    }

    /**
     * 导出用户信息Excel
     *
     * @param ids
     * @return
     */
    @Override
    public R<String> outputExcelByIds(String[] ids, String excelName) {
        if (ObjectUtils.isEmpty(ids) || StringUtils.isEmpty(excelName)) {
            return R.error("数据为空");
        }
        String excelName1 = excelName + ".xlsx";
        List<String> fileNames = localFileTask.getFileNames(userMessageExcel);
        if (null != fileNames && fileNames.size() != 0) {
            for (String fileName : fileNames) {
                if (StringUtils.equals(fileName, excelName1)) {
                    return R.error("文件名已存在");
                }
            }
        }

        ArrayList list = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            list.add(ids[i]);
        }
        List<UserMessage> list1 = userMessageMapper.selectBatchIds(list);
        String excelPath = userMessageExcel + excelName1;//后期可换minio地址
        File file = new File(excelPath);
        if (!file.getParentFile().exists()) { // 此时文件有父目录
            file.getParentFile().mkdirs(); // 创建父目录
        }
        ExcelTool.exportToFile(Collections.singletonList(list1), excelPath);
        return R.success(excelPath);
    }

    /**
     * 已知原始密码,修改密码
     *
     * @param request
     * @return
     */
    @Override
    public R<String> updatePassword(HttpServletRequest request) {
        String userId = JwtUtils.getUserIdByJwtToken(request);

        return null;
    }
}
