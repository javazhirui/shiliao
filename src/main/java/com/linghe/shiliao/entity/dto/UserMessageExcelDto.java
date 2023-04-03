package com.linghe.shiliao.entity.dto;

import com.xxl.tool.excel.annotation.ExcelField;
import com.xxl.tool.excel.annotation.ExcelSheet;
import lombok.Data;

import java.io.Serializable;

@Data
@ExcelSheet(name = "用户基本信息")
public class UserMessageExcelDto implements Serializable {

    private static final long serialVersionUID = -1576406777598711683L;

    /**
     * 用户姓名
     */
    @ExcelField(name = "姓名")
    private String name;

    /**
     * 0/未知(1男 2女)
     */
    @ExcelField(name = "性别")
    private String gender;

    /**
     * 用户年龄
     */
    @ExcelField(name = "年龄")
    private Integer age;

    /**
     * 身体健康情况
     */
    @ExcelField(name = "健康状况")
    private String health;

    /**
     * 联系电话
     */
    @ExcelField(name = "联系电话")
    private String phone;

    /**
     * 邮箱
     */
    @ExcelField(name = "邮箱")
    private String email;

    /**
     * 用户账号名
     */
    @ExcelField(name = "用户昵称")
    private String userName;

//    /**
//     * 0停用 1启用
//     */
//    @ExcelField(name = "账号状态 0-停用 1-启用")
//    private Integer status;
//
//    /**
//     * 权限id
//     */
//    @ExcelField(name = "权限")
//    private Integer ruleId;

    /**
     * 备注
     */
    @ExcelField(name = "备注")
    private String remark;
}
