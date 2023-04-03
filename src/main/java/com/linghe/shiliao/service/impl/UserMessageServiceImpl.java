package com.linghe.shiliao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linghe.shiliao.common.R;
import com.linghe.shiliao.entity.UserMessage;
import com.linghe.shiliao.entity.dto.LoginDto;
import com.linghe.shiliao.entity.dto.PasswordDto;
import com.linghe.shiliao.entity.dto.UserMessageDto;
import com.linghe.shiliao.mapper.UserMessageMapper;
import com.linghe.shiliao.service.UserMessageService;
import com.linghe.shiliao.task.LocalFileTask;
import com.linghe.shiliao.utils.JwtUtils;
import com.linghe.shiliao.utils.Md5Utils;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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

    /**
     * 修改客户基本信息
     * @param userMessage
     */
    @Override
    public void editUserMessageBean(UserMessage userMessage) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String thisDateTime = sdf.format(date);
        userMessage.setUpdateTime(thisDateTime);
        UserMessage userMessage2 = new UserMessage();
        BeanUtils.copyProperties(userMessage, userMessage2);
        this.updateById(userMessage2);
    }

    /**
     * 删除客户信息/隐藏客户信息
     * @param userMessage
     */
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
    public R<String> updatePassword(HttpServletRequest request, PasswordDto passwordDto) {
        String userId = JwtUtils.getUserIdByJwtToken(request);
        if (ObjectUtils.isEmpty(passwordDto) || passwordDto.getNewPassword().isEmpty() || passwordDto.getOldPassword().isEmpty()) {
            return R.error("输入信息有空,请检查");
        }

        UserMessage userMessage = userMessageMapper.selectById(userId);
        if (!StringUtils.equals(userMessage.getPassword(), Md5Utils.hash(passwordDto.getOldPassword()))) {
            return R.error("原密码错误");
        }
        userMessage.setPassword(passwordDto.getNewPassword());
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        userMessage.setUpdateTime(sdf.format(date));

        int i = userMessageMapper.updateById(userMessage);
        if (i < 1) {
            return R.error("密码修改失败");
        }

        return R.success("密码修改成功");
    }
//
//    @Override
//    public UserMessage getUserBean(LoginDto loginDto) {
//        LambdaQueryWrapper<UserMessage> lqw = new LambdaQueryWrapper<>();
//        lqw.eq(UserMessage::getEmail, loginDto.getUserName());
//        UserMessage userMessage = userMessageMapper.selectOne(lqw);
//        if (ObjectUtils.isEmpty(userMessage)) {
//            LambdaQueryWrapper<UserMessage> lqw1 = new LambdaQueryWrapper<>();
//            lqw1.eq(UserMessage::getUserName, loginDto.getUserName());
//            UserMessage userMessage1 = userMessageMapper.selectOne(lqw1);
//            return userMessage1;
//        }
//        return userMessage;
//    }

    /**
     * 添加用户个人信息
     *
     * @param userMessageDto
     */
    @Override
    public R<String> addUserBean(UserMessageDto userMessageDto) {

        //查询构造器
        LambdaQueryWrapper<UserMessage> lqw = new LambdaQueryWrapper<>();
        //查询email
        lqw.eq(UserMessage::getEmail, userMessageDto.getEmail());
        //获取第一个查询结果
        UserMessage userMessage = userMessageMapper.selectOne(lqw);
        if (!ObjectUtils.isEmpty(userMessage)) {
            return R.error("邮箱已注册,请更换邮箱或重新登陆");
        }

        LambdaQueryWrapper<UserMessage> lqw1 = new LambdaQueryWrapper<>();
        lqw1.eq(UserMessage::getUserName, userMessageDto.getUserName());
        UserMessage userMessage1 = userMessageMapper.selectOne(lqw1);
        if (!ObjectUtils.isEmpty(userMessage1)) {
            return R.error("账号名已存在");
        }

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String thisDateTime = sdf.format(date);

        userMessageDto.setCreateTime(thisDateTime);

        //对密码进行MD5加密后替换password
        if (StringUtils.isEmpty(userMessageDto.getPassword())) {
            return R.error("密码不可为空");
        }
        userMessageDto.setPassword(Md5Utils.hash(userMessageDto.getPassword()));
        userMessageMapper.insert(userMessageDto);

        return R.success("添加成功");
    }

    /**
     * 通过姓名查询所有相似名称的客户
     * @param userMessageDto
     * @return
     */
    @Override
    public R<List<UserMessage>> getUserMessages(UserMessageDto userMessageDto) {

        LambdaQueryWrapper<UserMessage> lqw = new LambdaQueryWrapper<>();
        lqw.like(UserMessage::getName,userMessageDto.getName());
        List<UserMessage> userMessages = userMessageMapper.selectList(lqw);
        if(ObjectUtils.isEmpty(userMessages)){
            return R.error("未查询到该用户信息");
        }


        return R.success(userMessages);
    }

}
