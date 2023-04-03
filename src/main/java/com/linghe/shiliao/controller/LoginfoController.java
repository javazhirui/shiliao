package com.linghe.shiliao.controller;

import com.linghe.shiliao.common.R;
import com.linghe.shiliao.entity.Loginfo;
import com.linghe.shiliao.entity.dto.LogDto;
import com.linghe.shiliao.service.LoginfoService;
import com.linghe.shiliao.utils.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("操作日志控制器")
@RestController
@RequestMapping("/loginfo")
public class LoginfoController {

    @Autowired
    private LoginfoService loginfoService;

    /**
     * 查询日志信息
     *
     * @param logDto 可传userIds((string字符串)多个用户userId);模糊查询:姓名name,电话号phone,邮箱email,
     *               操作类型logType,具体操作logMessage,当前页currentPage,单页条数pageSize
     * @return 回传分页数据
     */
    @ApiOperation("查询操作日志信息")
    @PostMapping("/getLog")
    public R<Page<Loginfo>> getLog(@RequestBody LogDto logDto) {
        return loginfoService.getLog(logDto);
    }
}
