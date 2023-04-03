package com.linghe.shiliao.controller;


import com.linghe.shiliao.common.R;
import com.linghe.shiliao.entity.Checks;
import com.linghe.shiliao.entity.dto.ChecksDto;
import com.linghe.shiliao.service.ChecksService;
import com.linghe.shiliao.utils.Page;
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
    @PostMapping("/getChecklist")
    public R<Page<ChecksDto>> getChecklist(@RequestBody ChecksDto checksDto) {
        return checksService.getChecklist(checksDto);
    }
}

