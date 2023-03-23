package com.linghe.shiliao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linghe.shiliao.common.R;
import com.linghe.shiliao.entity.Cases;
import com.linghe.shiliao.entity.UserMessage;
import com.linghe.shiliao.entity.dto.CasesDto;
import com.linghe.shiliao.mapper.CasesMapper;
import com.linghe.shiliao.mapper.UserMessageMapper;
import com.linghe.shiliao.service.CasesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linghe.shiliao.task.LocalFileTask;
import com.linghe.shiliao.utils.Page;
import com.linghe.shiliao.utils.WordUtil;
import com.xxl.tool.excel.ExcelTool;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhao_qin
 * @since 2023-03-14
 */
@Service
public class CasesServiceImpl extends ServiceImpl<CasesMapper, Cases> implements CasesService {

    @Autowired
    private CasesMapper casesMapper;

    @Autowired
    private LocalFileTask localFileTask;

    @Autowired
    private UserMessageMapper userMessageMapper;

    @Value("${shiliaoFilePath.casesMessageExcelPath}")
    private String casesMessageExcel;

    @Value("${shiliaoFilePath.casesMessageWordPath}")
    private String casesMessageWord;


    /**
     * 获取客户及病历信息
     *
     * @param status
     * @param phone
     * @param name
     * @param health
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public Page<CasesDto> getCaseList(Integer status, String phone, String name, String health, Integer currentPage,
                                      Integer pageSize) {
        Integer startSize = null;
        if (null != currentPage && null != pageSize) {
            startSize = (currentPage - 1) * pageSize;
        }
        Page<CasesDto> pageDto = new Page<>();
        pageDto.setCurrentPage(currentPage);
        pageDto.setPageSize(pageSize);
        pageDto.setTotal(casesMapper.getTotal(status, phone, name, health, startSize, pageSize));

        pageDto.setList(casesMapper.getList(status, phone, name, health, startSize, pageSize));
        return pageDto;
    }

    /**
     * 新增病历信息
     *
     * @param cases
     * @return
     */
    @Override
    public R<String> addCases(Cases cases) {

        if (null == cases) {
            return R.error("数据无效,请重新输入");
        }

        //mybatis-plus自带save方法,成功返回true,反之false
        boolean b = this.save(cases);

        if (!b) {
            return R.error("添加失败");
        }
        return R.success("添加成功");
    }

    /**
     * 编辑修改客户/病历信息
     *
     * @param cases
     * @return
     */
    @Override
    public R<String> updateCases(Cases cases) {

        if (null == cases) {
            return R.error("数据无效,请重新输入");
        }

        boolean b = this.updateById(cases);

        if (!b) {
            return R.error("修改失败");
        }

        return R.success("修改成功");
    }

    /**
     * 根据id数组导出excel
     *
     * @param ids
     * @param excelName
     * @return
     */
    @Override
    public R<String> outputExcelByIds(String[] ids, String excelName) {
        if (ObjectUtils.isEmpty(ids) || StringUtils.isEmpty(excelName)) {
            return R.error("数据为空");
        }
        String excelName1 = excelName + ".xlsx";
        List<String> fileNames = localFileTask.getFileNames(casesMessageExcel);
        if (null != fileNames && fileNames.size() != 0) {
            for (String fileName : fileNames) {
                if (StringUtils.equals(fileName, excelName1)) {
                    return R.error("文件名已存在");
                }
            }
        }

        List<CasesDto> list = casesMapper.getByIds(ids);
        String excelPath = casesMessageExcel + excelName + ".xlsx";//后期可换minio地址
        File file = new File(excelPath);
        if (!file.getParentFile().exists()) { // 此时文件有父目录
            file.getParentFile().mkdirs(); // 创建父目录
        }
        ExcelTool.exportToFile(Collections.singletonList(list), excelPath);

        return R.success(excelPath);
    }

    /**
     * 根据id数组导出word
     *
     * @param ids
     * @return
     */
    @Override
    public R<String> outputWordByIds(String[] ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return R.error("id为空");
        }
        List<CasesDto> list = casesMapper.getByIds(ids);
        for (int i = 0; i < ids.length; i++) {
            Map<String, Object> dataMap = new HashMap<>();
            UserMessage userMessage = userMessageMapper.selectById(ids[i]);
            dataMap.put("name", userMessage.getName());
            dataMap.put("gender", userMessage.getGender() == 1 ? "男" : "女");
            dataMap.put("age", userMessage.getAge());
            dataMap.put("phone", userMessage.getPhone());
            dataMap.put("email", userMessage.getEmail());
            LambdaQueryWrapper<Cases> lqw = new LambdaQueryWrapper<>();
            lqw.eq(Cases::getUserId, userMessage.getUserId());
            lqw.orderByDesc(Cases::getCreateTime);
            List<Cases> cases = casesMapper.selectList(lqw);
            List<Map<String, Object>> casesList = new LinkedList<>();
            for (Cases aCase : cases) {
                Map<String, Object> map = new HashMap<>();
                map.put("time", aCase.getCreateTime());
                map.put("diagnosis", aCase.getDiagnosis());
                map.put("feedback", aCase.getFeedback());
                casesList.add(map);
            }
            dataMap.put("casesList", casesList);
            WordUtil wordUtil = new WordUtil();
            String wordName = casesMessageWord + userMessage.getName() + "_" + userMessage.getUserName() + ".doc";
            File file = new File(wordName);
            if (!file.getParentFile().exists()) { // 此时文件有父目录
                file.getParentFile().mkdirs(); // 创建父目录
            }
            wordUtil.createWord(dataMap, "casesWord.xml", wordName);
        }
        return R.success("导出结束,请前往本地查看:" + casesMessageWord);
    }
}
