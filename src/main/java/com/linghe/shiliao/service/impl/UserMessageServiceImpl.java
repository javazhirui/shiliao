package com.linghe.shiliao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.linghe.shiliao.entity.UserMessage;
import com.linghe.shiliao.entity.dto.PageResponseResult;
import com.linghe.shiliao.entity.dto.ResponseResult;
import com.linghe.shiliao.entity.vo.UserMessageVo;
import com.linghe.shiliao.mapper.UserMessageMapper;
import com.linghe.shiliao.service.UserMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhao_qin
 * @since 2023-03-14
 */
@Service
public class UserMessageServiceImpl extends ServiceImpl<UserMessageMapper, UserMessage> implements UserMessageService {

    /**
     * 查询用户信息
     * @param userVo
     * @return
     */
    @Override
    public ResponseResult getList(UserMessageVo userVo) {

        //防空数据、判断分页数据是否合法
        userVo.checkParam();

        //创建基础分页构造器,参数(当前页,条数)
        IPage iPage = new Page(userVo.getCurrentPage(),userVo.getSize());
        //创建查询构造器
        LambdaQueryWrapper<UserMessage> lqw = new LambdaQueryWrapper<>();

        if (null != userVo.getPhone()) {
            //eq相当于sql里的"like"用法(表::列,条件值)
            lqw.like(UserMessage::getPhone,userVo.getPhone());
        }

        if (null != userVo.getEmail()) {
            lqw.like(UserMessage::getEmail,userVo.getEmail());
        }

        if (null != userVo.getName()) {
            lqw.like(UserMessage::getName,userVo.getName());
        }

        if (null != userVo.getMinAge() && null != userVo.getMaxAge()) {
            //(表::列,最小值,最大值)
            lqw.between(UserMessage::getAge,userVo.getMinAge(),userVo.getMinAge());
        }

        if (null != userVo.getMinCreatetime() && null != userVo.getMaxCreatetime()) {
            lqw.between(UserMessage::getCreateTime,userVo.getMinCreatetime(),userVo.getMaxCreatetime());
        }

        //根据创建时间降序
        lqw.orderByDesc(UserMessage::getCreateTime);

        //mybatis-plus的page方法参数(分页构造器,查询构造器)
        iPage = page(iPage,lqw);

        ResponseResult responseResult = new PageResponseResult(userVo.getCurrentPage(),userVo.getSize(),(int)iPage.getTotal());
        responseResult.setData(iPage.getRecords());
        return responseResult;
    }
}
