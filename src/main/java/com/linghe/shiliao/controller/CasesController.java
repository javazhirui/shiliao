package com.linghe.shiliao.controller;


import com.linghe.shiliao.common.R;
import com.linghe.shiliao.entity.Cases;
import com.linghe.shiliao.entity.dto.CasesDto;
import com.linghe.shiliao.entity.dto.UserMessageDto;
import com.linghe.shiliao.service.CasesService;
import com.linghe.shiliao.utils.MinIoUtils1;
import com.linghe.shiliao.utils.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhao_qin
 * @since 2023-03-14
 */
@ApiOperation("病例相关控制器")
@RestController
@RequestMapping("/cases")
public class CasesController {

    @Autowired
    private CasesService casesService;

    @Autowired
    private MinIoUtils1 minioUtils1;

    @Value("${minio.endpoint}")
    private String address;

    @Value("${minio.bucketName}")
    private String bucketName;

    @Value("${shiliaoFilePath.casesMessageExcelPath}")
    private String casesMessageExcel;

    @Value("${shiliaoFilePath.casesMessageWordPath}")
    private String casesMessageWord;

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
    @ApiOperation("添加客户/病历信息")
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
    @ApiOperation("修改客户/病历信息")
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
    @ApiOperation("根据id数组(暂时字符串代替:'1,2,3,10086')导出excel")
    @GetMapping("/outputExcelByIds")
    public R<String> outputExcelByIds(@RequestParam String excel, @RequestParam String excelName) {
        if (null == excel || excel.equals("")) {
            return R.error("请选择需要导出至excel的数据");
        }

        if (null == excelName || excelName.equals("")) {
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
    @ApiOperation("根据id数组(暂时字符串代替:'1,2,3,10086')导出word")
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
    @ApiOperation("获取登录用户自己的病例信息")
    @GetMapping("/getById")
    public R<List<Cases>> getById(HttpServletRequest request) {
        return casesService.getByUserId(request);
    }


    @ApiOperation("根据病例id删除(隐藏)病历信息")
    @PostMapping("/delCasesById")
    public R<String> delCasesById(@RequestBody Cases casesDto) {
        return casesService.delCasesById(casesDto);
    }

    /**
     * 食用疗程影像上传
     *
     * @param file
     * @return
     */
    @ApiOperation("食用疗程影像上传")
    @PostMapping("/getCaseUrl")
    public R<String> caseUrl(@RequestParam(value = "casesUrlFile", required = false) MultipartFile file, UserMessageDto userMessageDto) {
        return casesService.uploadFile(file, userMessageDto);
    }


    /**
     * 诊断图片上传
     *
     * @param file
     * @return
     */
    @ApiOperation("诊断图片上传")
    @PostMapping("/getCaseUrlImg")
    public R<String> getCaseUrlImg(@RequestParam(value = "casesUrlImgFile", required = false) MultipartFile file, UserMessageDto userMessageDto) {
        return casesService.uploadFile(file, userMessageDto);
    }

    /**
     * 食用疗程影像上传
     *
     * @param file
     * @return
     */
    @ApiOperation("用户文件通用上传接口")
    @PostMapping("/getCaseUrl")
    public R<String> uploadUserFile(@RequestParam(value = "userFile", required = false) MultipartFile file, UserMessageDto userMessageDto) {
        List<String> upload = minioUtils1.upload(new MultipartFile[]{file}, userMessageDto);
        String fileUrl = address + "/" + bucketName + "/" + upload.get(0);
        return R.success(fileUrl);
    }


}

