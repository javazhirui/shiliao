package com.linghe.shiliao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linghe.shiliao.entity.Loginfo;
import com.linghe.shiliao.mapper.LoginfoMapper;
import com.linghe.shiliao.service.LoginfoService;
import org.springframework.stereotype.Service;

@Service
public class LoginfoServiceImpl extends ServiceImpl<LoginfoMapper, Loginfo> implements LoginfoService {
}
