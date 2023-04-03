package com.linghe.shiliao.entity.dto;

import com.xxl.tool.excel.annotation.ExcelField;
import com.xxl.tool.excel.annotation.ExcelSheet;
import lombok.Data;

import java.io.Serializable;

@Data
@ExcelSheet(name = "用户信息")
public class UserMessageOutDto implements Serializable {

    private static final long serialVersionUID = -8092031019126079465L;

    @ExcelField(name = "姓名")
    private String name;

    @ExcelField(name = "年龄")
    private String age;

    @ExcelField(name = "性别")
    private String gender;

    @ExcelField(name = "联系电话")
    private String phone;

    @ExcelField(name = "邮箱")
    private String email;

    @ExcelField(name = "身体健康状况")
    private String health;

    @ExcelField(name = "创建时间")
    private String creatTime;

    @ExcelField(name = "修改时间")
    private String updateTime;
}
