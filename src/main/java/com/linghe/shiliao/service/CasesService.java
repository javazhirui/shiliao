package com.linghe.shiliao.service;

import com.linghe.shiliao.common.R;
import com.linghe.shiliao.entity.Cases;
import com.baomidou.mybatisplus.extension.service.IService;
import com.linghe.shiliao.entity.dto.CasesDto;
import com.linghe.shiliao.utils.Page;

import javax.servlet.http.HttpServletRequest;
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
    Page<CasesDto> getCaseList(Integer status, String phone, String name, String health, Integer currentPage, Integer pageSize);

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
     * @param excelName
     * @return
     */
    R<String> outputExcelByIds(String[] ids, String excelName);

    /**
     * 根据id数组导出word
     * @param ids
     * @return
     */
    R<String> outputWordByIds(String[] ids);

    /**
     * 获取登录用户自己的信息
     * @param request
     * @return
     */
    R<List<Cases>> getByUserId(HttpServletRequest request);

    R<String> addCasesInput(CasesDto casesDto);

    /**
     * 根据病例id删除/隐藏病例信息
     * @param casesId
     * @return
     */
    R<String> delCasesById(Cases cases);
}
