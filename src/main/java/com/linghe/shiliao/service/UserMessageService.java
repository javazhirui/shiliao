package com.linghe.shiliao.service;

import com.linghe.shiliao.entity.UserMessage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.linghe.shiliao.entity.vo.UserMessageVo;
import com.linghe.shiliao.utils.Page;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhao_qin
 * @since 2023-03-14
 */
public interface UserMessageService extends IService<UserMessage> {

    /**
     * 查询用户信息
     * @param userVo
     * @return
     */
    Page<UserMessage> getList(UserMessageVo userVo);
}
