package com.linghe.shiliao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linghe.shiliao.entity.UserMessage;
import com.linghe.shiliao.entity.dto.CasesDto;
import com.linghe.shiliao.service.UserMessageService;
import com.linghe.shiliao.utils.WordUtil;
import com.xxl.tool.excel.ExcelTool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.KeyGenerator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Slf4j
@SpringBootTest
class ShiliaoApplicationTests {

    @Autowired
    private UserMessageService userMessageService;

    @Test
    void readExcelTest() {
        List<Object> objects = ExcelTool.importExcel("D:/ShiLiao/casesMessageExcel/123333.xlsx", CasesDto.class);
        log.info(objects.toString());
    }

    @Test
    void readWord() throws FileNotFoundException {
        String file = "D:/ShiLiao/会议记录表单.docx";
        FileInputStream fileInputStream = new FileInputStream(new File(file));
        // 截取文件后缀
        String suffix = file.substring(file.lastIndexOf("."));
        // 调用工具类
        String result = WordUtil.readWord(suffix, fileInputStream);
        log.info(result);
    }

    @Test
    void test001() {
        LambdaQueryWrapper<UserMessage> lqw = new LambdaQueryWrapper<>();
        lqw.select(
                UserMessage.class, columns -> !columns.getColumn().equals("password")
                        && !columns.getColumn().equals("user_name")
//                        && !columns.getColumn().equals("name")
                        && !columns.getColumn().equals("rule_id")
//                      经测试,查询过滤不掉id属性
                        && !columns.getColumn().equals("user_id")
        );
        log.info(userMessageService.list(lqw).toString());
    }

    @Test
    void test002() {
        LambdaQueryWrapper<UserMessage> lqw = new LambdaQueryWrapper<>();
        lqw.select(UserMessage::getName, UserMessage::getUserName);
        lqw.orderByDesc(UserMessage::getName);
        log.info(userMessageService.list(lqw).toString());
    }



}
