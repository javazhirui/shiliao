package com.linghe.shiliao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linghe.shiliao.common.R;
import com.linghe.shiliao.entity.Loginfo;
import com.linghe.shiliao.entity.dto.LogDto;
import com.linghe.shiliao.utils.Page;

public interface LoginfoService extends IService<Loginfo> {

    /**
     * 查询日志信息
     * @param logDto 可传userIds((string字符串)多个用户userId);模糊查询:姓名name,电话号phone,邮箱email,
     *               操作类型logType,具体操作logMessage,当前页currentPage,单页条数pageSize
     * @return 回传分页数据
     */
    R<Page<Loginfo>> getLog(LogDto logDto);
}
