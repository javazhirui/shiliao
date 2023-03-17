package com.linghe.shiliao.service.impl;

import com.linghe.shiliao.common.R;
import com.linghe.shiliao.entity.UserMessage;
import com.linghe.shiliao.entity.dto.UserMessageDto;
import com.linghe.shiliao.mapper.UserMessageMapper;
import com.linghe.shiliao.service.UserService;
import com.linghe.shiliao.utils.Md5Utils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMessageMapper userMessageMapper;

    @Override
    public R<String> register(UserMessageDto userMessageDto) {
        try {
            if (ObjectUtils.isEmpty(userMessageDto)) {
                return R.error("数据为空,请重新编辑");
            }
            if (StringUtils.isBlank(userMessageDto.getCode())){
                return R.error("请重新输入验证码");
            }

            String code = userMessageDto.getCode();
            //对密码进行MD5加密后替换password
            userMessageDto.setPassword(Md5Utils.hash(userMessageDto.getPassword()));
            UserMessage userMessage = new UserMessage();
            //将userMessageDto中数据拷贝到userMessage
            BeanUtils.copyProperties(userMessageDto, userMessage);
            userMessageMapper.insert(userMessage);
        } catch (Exception e) {
            return R.error(e.toString());
        }
        return R.success("注册成功");
    }
}
