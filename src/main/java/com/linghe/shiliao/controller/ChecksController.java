package com.linghe.shiliao.controller;


import com.linghe.shiliao.common.R;
import com.linghe.shiliao.entity.Checks;
import com.linghe.shiliao.entity.dto.ChecksDto;
import com.linghe.shiliao.service.ChecksService;
import com.linghe.shiliao.utils.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhao_qin
 * @since 2023-03-14
 */
@Api("预约相关控制器")
@RestController
@RequestMapping("/checks")
public class ChecksController {

    @Autowired
    private ChecksService checksService;

    /**
     * 新增预约
     *
     * @param checks
     * @return
     */
    @ApiOperation("新增预约")
    @PostMapping("/addCheck")
    public R<String> addCheck(@RequestBody Checks checks) {
        return checksService.addCheck(checks);
    }

    /**
     * 处理预约
     *
     * @param checks
     * @return
     */
    @ApiOperation("处理预约")
    @PostMapping("/updateCheck")
    public R<String> updateCheck(@RequestBody Checks checks) {
        return checksService.updateCheck(checks);
    }

    /**
     * 获取待处理预约信息
     *
     * @param checksDto
     * @return
     */
    @ApiOperation("获取待处理预约信息")
    @PostMapping("/getChecklist")
    public R<Page<ChecksDto>> getChecklist(@RequestBody ChecksDto checksDto) {
        return checksService.getChecklist(checksDto);
    }
}

