package com.linghe.shiliao.service;

import com.linghe.shiliao.common.R;
import com.linghe.shiliao.entity.Cases;
import com.baomidou.mybatisplus.extension.service.IService;
import com.linghe.shiliao.entity.dto.CasesDto;
import com.linghe.shiliao.utils.Page;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhao_qin
 * @since 2023-03-14
 */
public interface CasesService extends IService<Cases> {

    /**
     * 获取客户及病历信息
     * @param status
     * @param phone
     * @param name
     * @param health
     * @param currentPage
     * @param pageSize
     * @return
     */
    Page<CasesDto> getCaseList(String status, String phone, String name, String health, Integer currentPage, Integer pageSize);

    /**
     * 新增病历信息
     * @param cases
     * @return
     */
    R<String> addCases(Cases cases);

    /**
     * 编辑修改客户/病历信息
     * @param cases
     * @return
     */
    R<String> updateCases(Cases cases);

    /**
     * 根据id数组导出excel
     * @param ids
     * @return
     */
    R<String> outputExcelByIds(String[] ids);
}
