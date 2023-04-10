package com.linghe.shiliao.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 文件管理对象 oss_file
 */
public class OssFile extends BaseEntity {

    private static final long serialVersionUID = 3434534775831371490L;

    /**
     * 主键
     */
    private Long fileId;

    /**
     * 文件上传后重新命名
     */
    private String fileKey;

    /**
     * 原始文件名
     */
    private String fileName;

    /**
     * 文件夹名称
     */
    private String fileDir;

    /**
     * 文件长度
     */
    private Long fileLength;

    /**
     * 文件扩展名
     */
    private String fileExtension;

    /**
     * 文件业务类型
     */
    private String businessType;


    private String businessTypeName;

    /**
     * 文件业务ID
     */
    private Long businessId;

    /**
     * 文件业务编码
     */
    private String businessNo;

    /**
     * 创建部门
     */
    private Long orgId;

    /**
     * 状态
     */
    private String state;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    private String dr;

    /**
     * 时间戳
     */
    private Date ts;


    private Long[] fileIds;

    /**
     * 资源访问地址
     */
    private String url;

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileDir(String fileDir) {
        this.fileDir = fileDir;
    }

    public String getFileDir() {
        return fileDir;
    }

    public void setFileLength(Long fileLength) {
        this.fileLength = fileLength;
    }

    public Long getFileLength() {
        return fileLength;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getBusinessType() {
        return businessType;
    }

    public String getBusinessTypeName() {
        return businessTypeName;
    }

    public void setBusinessTypeName(String businessTypeName) {
        this.businessTypeName = businessTypeName;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setDr(String dr) {
        this.dr = dr;
    }

    public String getDr() {
        return dr;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }

    public Date getTs() {
        return ts;
    }

    public void setFileIds(Long[] fileIds) {
        this.fileIds = fileIds;
    }

    public Long[] getFileIds() {
        return fileIds;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("fileId", getFileId())
                .append("fileKey", getFileKey())
                .append("fileName", getFileName())
                .append("fileDir", getFileDir())
                .append("fileLength", getFileLength())
                .append("fileExtension", getFileExtension())
                .append("businessType", getBusinessType())
                .append("businessId", getBusinessId())
                .append("businessNo", getBusinessNo())
                .append("orgId", getOrgId())
                .append("state", getState())
                .append("dr", getDr())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createDate", getCreateDate())
                .append("updateBy", getUpdateBy())
                .append("updateDate", getUpdateDate())
                .append("ts", getTs())
                .toString();
    }
}
