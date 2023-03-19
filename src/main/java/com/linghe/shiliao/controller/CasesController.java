package com.linghe.shiliao.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.linghe.shiliao.common.R;
import com.linghe.shiliao.entity.Cases;
import com.linghe.shiliao.entity.dto.CasesDto;
import com.linghe.shiliao.mapper.CasesMapper;
import com.linghe.shiliao.service.CasesService;
import com.linghe.shiliao.utils.Page;
import com.xxl.tool.excel.ExcelTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhao_qin
 * @since 2023-03-14
 */
@RestController
@RequestMapping("/cases")
public class CasesController {

    @Autowired
    private CasesService casesService;

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
    @PostMapping("/getCaseList")
    public Page<CasesDto> getCaseList(String status, String phone, String name, String health, Integer currentPage, Integer pageSize) {
        return casesService.getCaseList(status,phone, name, health, currentPage, pageSize);
    }

    /**
     * 添加客户/病历信息
     * @param cases
     * @return
     */
    @PostMapping("/addCases")
    public R<String> addCases(@RequestBody Cases cases) {
        return casesService.addCases(cases);
    }

    /**
     * 修改客户/病历信息
     * @param cases
     * @return
     */
    @PostMapping("/updateCases")
    public R<String> updateCases(@RequestBody Cases cases) {
        return casesService.updateCases(cases);
    }

    /**
     * 根据id数组导出excel
     * @param ids
     * @return
     */
    @PostMapping("/outputExcelByIds")
    public R<String> outputExcelByIds(@RequestBody String[] ids) {
        return casesService.outputExcelByIds(ids);
    }

//    @PostMapping("/outputExcel")
//    public R<String> outputExcel()
}

