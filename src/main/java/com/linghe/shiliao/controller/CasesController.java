package com.linghe.shiliao.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.linghe.shiliao.aop.LogAop;
import com.linghe.shiliao.aop.RuleAop;
import com.linghe.shiliao.common.R;
import com.linghe.shiliao.entity.Cases;
import com.linghe.shiliao.entity.UserMessage;
import com.linghe.shiliao.entity.dto.CasesDto;
import com.linghe.shiliao.entity.dto.UserMessageDto;
import com.linghe.shiliao.mapper.CasesMapper;
import com.linghe.shiliao.service.CasesService;
import com.linghe.shiliao.utils.JwtUtils;
import com.linghe.shiliao.utils.Page;
import com.xxl.tool.excel.ExcelTool;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.support.HttpRequestHandlerServlet;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
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
     * @param userMessageDto
     * @return
     */
    @PostMapping("/getCaseList")
    public Page<CasesDto> getCaseList(@RequestBody UserMessageDto userMessageDto) {
        return casesService.getCaseList(userMessageDto.getStatus(), userMessageDto.getPhone(), userMessageDto.getName(), userMessageDto.getHealth(), userMessageDto.getCurrentPage(), userMessageDto.getPageSize());
    }

    /**
     * 添加客户/病历信息
     *
     * @param cases
     * @return
     */
    @PostMapping("/addCases")
    public R<String> addCases(@RequestBody Cases cases) {
        return casesService.addCases(cases);
    }

    /**
     * 修改客户/病历信息
     *
     * @param cases
     * @return
     */
    @PostMapping("/updateCases")
    public R<String> updateCases(@RequestBody Cases cases) {
        return casesService.updateCases(cases);
    }

    /**
     * 病例信息导出
     * 根据id数组导出excel
     *
     * @param excel
     * @param excelName
     * @return
     */
//    @RuleAop(ruleNames = "1")
    @GetMapping("/outputExcelByIds")
    public R<String> outputExcelByIds(@RequestParam String excel, @RequestParam String excelName) {
        if(null == excel || excel.equals("")){
            return R.error("请选择需要导出至excel的数据");
        }

        if(null == excelName || excelName.equals("")){
            return R.error("文件名称不能为空");
        }

        String[] ids = excel.split(",");
        return casesService.outputExcelByIds(ids, excelName);
    }

    /**
     * 根据id数组导出word
     *
     * @param word
     * @return
     */
//    @RuleAop(ruleNames = "0")
//    @LogAop(logType = "查询导出文件", logMessage = "根据ids导出病例word")
    @GetMapping("/outputWordByIds")
    public R<String> outputWordByIds(@RequestParam String word) {
        String[] ids = word.split(",");
        return casesService.outputWordByIds(ids);
    }

    /**
     * 获取登录用户自己的病例信息
     *
     * @param request
     * @return
     */
    @GetMapping("/getById")
    public R<List<Cases>> getById(HttpServletRequest request) {
        return casesService.getByUserId(request);
    }

    /**
     * 病例录入
     * @return
     */
    @PostMapping("/addCasesInput")
    public R<String> addCasesInput(@RequestBody CasesDto casesDto){
        return casesService.addCasesInput(casesDto);
    }

    /**
     * 根据病例id删除/隐藏病例信息
     * @param cases
     * @return
     */
    @PostMapping("/delCasesById")
    public R<String> delCasesById(@RequestBody Cases cases){
        return casesService.delCasesById(cases);
    }


}

