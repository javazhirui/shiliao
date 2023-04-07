package com.linghe.shiliao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linghe.shiliao.common.R;
import com.linghe.shiliao.entity.Cases;
import com.linghe.shiliao.entity.UserMessage;
import com.linghe.shiliao.entity.dto.CasesDto;
import com.linghe.shiliao.entity.dto.Dto;
import com.linghe.shiliao.entity.dto.UserMessageDto;
import com.linghe.shiliao.mapper.CasesMapper;
import com.linghe.shiliao.mapper.UserMessageMapper;
import com.linghe.shiliao.service.CasesService;
import com.linghe.shiliao.task.LocalFileTask;
import com.linghe.shiliao.utils.JwtUtils;
import com.linghe.shiliao.utils.Page;
import com.linghe.shiliao.utils.WordUtil;
import com.xxl.tool.excel.ExcelTool;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
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

    @Value("${shiliaoFilePath.casesUrlPath}")
    private String casesUrlPath;

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

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cases.setCreateTime(sdf.format(date));

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

        List<Dto> list = casesMapper.getByIds(ids);
        String excelPath = casesMessageExcel + excelName + ".xlsx";//后期可换minio地址
        File file = new File(excelPath);
        if (!file.getParentFile().exists()) { // 此时文件有父目录
            file.getParentFile().mkdirs(); // 创建父目录
        }
        ExcelTool.exportToFile(Collections.singletonList(list), excelPath);

        return R.success("导出结束,请前往本地查看:" + casesMessageExcel);
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
        //循环遍历id做查询导出
        for (String id : ids) {
            Map<String, Object> dataMap = new HashMap<>();
            //获取用户基础信息
            UserMessage userMessage = userMessageMapper.selectById(id);
            dataMap.put("name", userMessage.getName());
            dataMap.put("gender", userMessage.getGender());
            dataMap.put("age", userMessage.getAge());
            dataMap.put("phone", userMessage.getPhone());
            dataMap.put("email", userMessage.getEmail());
            //根据userId查询对应病例,按时间降序
            LambdaQueryWrapper<Cases> lqw = new LambdaQueryWrapper<>();
            lqw.eq(Cases::getUserId, userMessage.getUserId());
            lqw.orderByDesc(Cases::getCreateTime);
            List<Cases> cases = casesMapper.selectList(lqw);
            //将查询到的病例信息存入表格所属集合
            List<Map<String, Object>> casesList = new LinkedList<>();
            Map<String, Object> map = new HashMap<>();
            if (cases != null && cases.size() != 0) {
                for (Cases aCase : cases) {
                    map.put("time", aCase.getCreateTime());
                    map.put("diagnosis", aCase.getDiagnosis());
                    map.put("feedback", aCase.getFeedback());
                    casesList.add(map);
                }
            } else {
                map.put("time", "null");
                map.put("diagnosis", "null");
                map.put("feedback", "null");
                casesList.add(map);
            }

            dataMap.put("casesList", casesList);
            //文件生成位置
            String wordName = casesMessageWord + userMessage.getName() + "_" + userMessage.getPhone() + ".doc";
            File file = new File(wordName);
            if (!file.getParentFile().exists()) { // 此时文件有父目录
                file.getParentFile().mkdirs(); // 创建父目录
            }

            WordUtil wordUtil = new WordUtil();
            wordUtil.createWord(dataMap, "casesWord.xml", wordName);
        }
        return R.success("导出结束,请前往本地查看:" + casesMessageWord);
    }

    /**
     * 获取登录用户自己的信息
     *
     * @param request
     * @return
     */
    @Override
    public R<List<Cases>> getByUserId(HttpServletRequest request) {
        LambdaQueryWrapper<Cases> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Cases::getUserId, JwtUtils.getUserIdByJwtToken(request));
        lqw.orderByDesc(Cases::getCreateTime);
        List<Cases> casesList = casesMapper.selectList(lqw);
        if (ObjectUtils.isEmpty(casesList)) {
            return R.error("当前用户没有病例信息");
        }
        return R.success(casesList);
    }

    /**
     * 根据病例id删除/隐藏病例信息
     *
     * @param cases
     * @return
     */
    @Override
    public R<String> delCasesById(Cases cases) {
        try {
            casesMapper.updateById(cases);
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
        return R.success("删除成功");
    }

    /**
     * 文件上传方法提取
     * @param file
     * @param userMessageDto
     * @return
     */
    @Override
    public R<String> uploadFile(MultipartFile file, UserMessageDto userMessageDto) {
        if (ObjectUtils.isEmpty(file)) {
            return R.error("文件上传错误，请重新上传");
        }

        String filename = file.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));
        int i = 0;
        String[] suffixArr = FileSuffix.suffixArr;
        for (String s : suffixArr) {
            i++;
            if (StringUtils.equalsIgnoreCase(s, suffix)) {
                break;
            }
        }
        if (i == suffixArr.length) {
            return R.error("文件格式错误,请重新上传");
        }

        File casesUrlFileName = new File(casesUrlPath + "/" + userMessageDto.getName() + "_" + userMessageDto.getPhone() + "/" + filename);
        if (!casesUrlFileName.exists()) {
            casesUrlFileName.mkdirs();
        }
        List<String> fileNames = localFileTask.getFileNames(casesUrlPath);
        if (null != fileNames && fileNames.size() != 0) {
            for (String fileName : fileNames) {
                if (StringUtils.equals(fileName, filename)) {
                    return R.error("文件名已存在");
                }
            }
        }
        try {
            file.transferTo(casesUrlFileName);
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
        return R.success("文件上传成功,文件路径为: " + casesUrlFileName);
    }

    /**
     * 通过客户登录id查询该客户所有的病例信息
     * @param userId
     * @return
     */
    @Override
    public R<List<Cases>> getCaseList(Long userId) {
        if(null == userId || userId.equals("") || userId == 0){
            return R.error("请重新登录用户信息");
        }
        LambdaQueryWrapper<Cases> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Cases::getUserId, userId);
        lqw.orderByDesc(Cases::getCreateTime);
        List<Cases> casesList = casesMapper.selectList(lqw);
        return R.success(casesList);
    }

}
