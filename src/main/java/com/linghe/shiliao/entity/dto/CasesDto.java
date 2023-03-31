package com.linghe.shiliao.entity.dto;

import com.xxl.tool.excel.annotation.ExcelField;
import com.xxl.tool.excel.annotation.ExcelSheet;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ExcelSheet(name = "病例")
public class CasesDto implements Serializable {
    private static final long serialVersionUID = 7715737320790709138L;

    private Integer pageSize;
    private Integer currentPage;

    /**
     * 用户id
     */
    private Long userId;
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
     * 用户姓名
     */

    private String name;

    /**
     * 用户年龄
     */
    private Integer age;

    /**
     * 0/未知(1男 2女)
     */
    private String gender;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 身体健康情况
     */
    private String health;

    /**
     * 用户账号(默认为手机号)
     */
    private String userName;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 0停用 1启用
     */
    private Integer status;

    /**
     * 病例id
     */
    @ExcelField(name = "case_id")
    private String caseId;

    /**
     * 权限id
     */
    private Integer ruleId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改时间
     */
    private String updateTime;
}
