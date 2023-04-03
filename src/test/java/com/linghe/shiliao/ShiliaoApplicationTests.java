package com.linghe.shiliao;

import com.linghe.shiliao.entity.dto.CasesDto;
import com.xxl.tool.excel.ExcelTool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Api("测试类")
@SpringBootTest
class ShiliaoApplicationTests {

    @ApiOperation("读取excel信息测试")
    @Test
    void readExcelTest() {
        List<Object> objects = ExcelTool.importExcel("D:/ShiLiao/casesMessageExcel/123333.xlsx", CasesDto.class);
        System.out.println(objects);
    }

}
