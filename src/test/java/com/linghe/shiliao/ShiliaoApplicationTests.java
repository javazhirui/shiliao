package com.linghe.shiliao;

import com.linghe.shiliao.entity.dto.CasesDto;
import com.linghe.shiliao.utils.WordUtil;
import com.xxl.tool.excel.ExcelTool;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

@Slf4j
@SpringBootTest
class ShiliaoApplicationTests {


    @Test
    void readExcelTest() {
        List<Object> objects = ExcelTool.importExcel("D:/ShiLiao/casesMessageExcel/123333.xlsx", CasesDto.class);
        System.out.println(objects);
    }

    @Test
    void readWord() throws FileNotFoundException {
        String file = "D:/ShiLiao/会议记录表单.docx";
        FileInputStream fileInputStream = new FileInputStream(new File(file));
        // 截取文件后缀
        String suffix = file.substring(file.lastIndexOf("."));
        // 调用工具类
        String result = WordUtil.readWord(suffix, fileInputStream);
        System.out.println(result);
    }
}
