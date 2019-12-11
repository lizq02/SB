package com.lzq.selfdiscipline.business.bean;

import java.util.Date;

/**
 * 文件管理表
 */
public class FileManagerBean {
    // id
    private String id;
    // 文件名称
    private String fileName;
    // 文件类型
    private String fileType;
    // 文件大小
    private long fileSize;
    // 文件相对路径
    private String fileUrl;
    // 文件可预览相对路径
    private String filePreviewUrl;
    // 添加时间
    private Date addTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFilePreviewUrl() {
        return filePreviewUrl;
    }

    public void setFilePreviewUrl(String filePreviewUrl) {
        this.filePreviewUrl = filePreviewUrl;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
}
