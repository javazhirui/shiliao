package com.linghe.shiliao;

import com.linghe.shiliao.entity.dto.CasesDto;
import com.linghe.shiliao.entity.dto.UserMessageOutDto;
import com.xxl.tool.excel.ExcelTool;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ShiliaoApplicationTests {

    @Test
    void readExcelTest() {
        List<Object> objects = ExcelTool.importExcel("D:/ShiLiao/casesMessageExcel/123333.xlsx", CasesDto.class);
        System.out.println(objects);
    }

}
