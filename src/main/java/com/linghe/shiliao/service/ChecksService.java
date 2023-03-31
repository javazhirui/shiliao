package com.linghe.shiliao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linghe.shiliao.common.R;
import com.linghe.shiliao.entity.Checks;
import com.linghe.shiliao.entity.dto.ChecksDto;
import com.linghe.shiliao.utils.Page;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zhao_qin
 * @since 2023-03-14
 */
public interface ChecksService extends IService<Checks> {

    /**
     * 新增预约
     *
     * @param checks
     * @return
     */
    R<String> addCheck(Checks checks);

    /**
     * 处理预约
     *
     * @param checks
     * @return
     */
    R<String> updateCheck(Checks checks);

    /**
     * 获取待处理预约信息
     *
     * @param checksDto
     * @return
     */
    R<Page<ChecksDto>> getChecklist(ChecksDto checksDto);
}
