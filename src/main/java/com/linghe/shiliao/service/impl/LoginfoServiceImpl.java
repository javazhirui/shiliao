package com.linghe.shiliao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linghe.shiliao.common.R;
import com.linghe.shiliao.entity.Loginfo;
import com.linghe.shiliao.entity.UserMessage;
import com.linghe.shiliao.entity.dto.LogDto;
import com.linghe.shiliao.mapper.LoginfoMapper;
import com.linghe.shiliao.mapper.UserMessageMapper;
import com.linghe.shiliao.service.LoginfoService;
import com.linghe.shiliao.service.UserMessageService;
import com.linghe.shiliao.utils.Page;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class LoginfoServiceImpl extends ServiceImpl<LoginfoMapper, Loginfo> implements LoginfoService {

    @Autowired
    private LoginfoMapper loginfoMapper;

    @Autowired
    private UserMessageMapper userMessageMapper;

    /**
     * 查询日志信息
     *
     * @param logDto 可传userIds((string字符串)多个用户userId);模糊查询:姓名name,电话号phone,邮箱email,
     *               操作类型logType,具体操作logMessage,当前页currentPage,单页条数pageSize
     * @return 回传分页数据
     */
    @Override
    public R<Page<Loginfo>> getLog(LogDto logDto) {
        if (ObjectUtils.isEmpty(logDto)) {
            return R.error("请求参数为空,请检查");
        }
        if (logDto.getCurrentPage() == null || logDto.getPageSize() == null) {
            return R.error("分页参数为空");
        }
        String name = logDto.getName();
        String phone = logDto.getPhone();
        String email = logDto.getEmail();
        List<Long> userIds = new ArrayList<>();
        if (StringUtils.isNotEmpty(logDto.getIds())) {
            String[] ids = logDto.getIds().split(",");
            for (String id : ids) {
                userIds.add(Long.parseLong(id));
            }
        }
        if (StringUtils.isNotEmpty(name) || StringUtils.isNotEmpty(phone) || StringUtils.isNotEmpty(email)) {
            LambdaQueryWrapper<UserMessage> userLqw = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(name)) {
                userLqw.like(UserMessage::getName, name);
            }
            if (StringUtils.isNotEmpty(phone)) {
                userLqw.like(UserMessage::getPhone, phone);
            }
            if (StringUtils.isNotEmpty(email)) {
                userLqw.like(UserMessage::getEmail, email);
            }

            List<UserMessage> userMessages = userMessageMapper.selectList(userLqw);
            for (UserMessage userMessage : userMessages) {
                userIds.add(userMessage.getUserId());
            }
        }


        LambdaQueryWrapper<Loginfo> lqw = new LambdaQueryWrapper<>();
        if (ObjectUtils.isNotEmpty(userIds) && userIds.size() != 0) {
            lqw.in(Loginfo::getUserId, userIds);
        }
        if (StringUtils.isNotEmpty(logDto.getStartTime()) && StringUtils.isNotEmpty(logDto.getEndTime())) {
            lqw.between(Loginfo::getCreateTime, logDto.getStartTime(), logDto.getEndTime());
        }
        if (StringUtils.isNotEmpty(logDto.getLogType())) {
            lqw.like(Loginfo::getLogType, logDto.getLogType());
        }
        if (StringUtils.isNotEmpty(logDto.getLogMessage())) {
            lqw.like(Loginfo::getLogMessage, logDto.getLogMessage());
        }
        Long total = loginfoMapper.selectCount(lqw);
        lqw.orderByDesc(Loginfo::getUserId);
        lqw.orderByDesc(Loginfo::getCreateTime);

        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Loginfo> page =
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(logDto.getCurrentPage(), logDto.getPageSize());
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Loginfo> loginfoPage = this.page(page, lqw);
        List<Loginfo> records = loginfoPage.getRecords();
        Page<Loginfo> loginfoPage1 = new Page<>();
        loginfoPage1.setCurrentPage(logDto.getCurrentPage());
        loginfoPage1.setPageSize(logDto.getPageSize());
        loginfoPage1.setTotal(total);
        loginfoPage1.setList(records);
        return R.success(loginfoPage1);
    }
}
