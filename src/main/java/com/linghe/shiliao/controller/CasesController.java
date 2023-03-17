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
     * @param phone
     * @param name
     * @param health
     * @param currentPage
     * @param pageSize
     * @return
     */
    @PostMapping("/getCaseList")
    public Page<CasesDto> getCaseList(String phone, String name, String health, Integer currentPage, Integer pageSize) {
        return casesService.getCaseList(phone, name, health, currentPage, pageSize);
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

    @GetMapping("/ceshi")
    public String ceshi(){
        casesService.ceshi();
        return "测试";
    }

//    @PostMapping("/outputExcel")
//    public R<String> outputExcel()
}

