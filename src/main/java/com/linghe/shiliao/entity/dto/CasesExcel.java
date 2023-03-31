package com.linghe.shiliao.entity.dto;

import com.xxl.tool.excel.annotation.ExcelField;
import com.xxl.tool.excel.annotation.ExcelSheet;
import lombok.Data;

import java.io.Serializable;

@Data
@ExcelSheet(name = "病例")
public class CasesExcel implements Serializable {
    private static final long serialVersionUID = 3626950822839403506L;

    /**
     * 用户姓名
     */
    @ExcelField(name = "姓名")
    private String name;

    /**
     * 性别
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
    @ExcelField(name = "身体健康状况")
    private String health;

    /**
     * 诊断结果
     */
    @ExcelField(name = "诊断结果")
    private String diagnosis;

    /**
     * 处理意见
     */
    @ExcelField(name = "处理意见")
    private String feedback;

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
     * 食疗视频路径
     */
    @ExcelField(name = "食疗视频路径")
    private String casesUrl;

    /**
     * 病例图片路径
     */
    @ExcelField(name = "病例图片路径")
    private String casesImgUrl;

    /**
     * 用户账号名
     */
    @ExcelField(name = "账号昵称")
    private String userName;

    /**
     * 账号状态 0停用 1启用
     */
    @ExcelField(name = "账号状态 0停用 1启用")
    private Integer status;

    /**
     * 创建时间
     */
    @ExcelField(name = "时间")
    private String createTime;

    /**
     * 备注
     */
    @ExcelField(name = "备注信息")
    private String remark;
}
