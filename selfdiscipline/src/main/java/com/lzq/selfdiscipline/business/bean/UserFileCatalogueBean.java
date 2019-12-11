package com.lzq.selfdiscipline.business.bean;

import java.util.Date;

/**
 * 用户文件目录
 */
public class UserFileCatalogueBean {
    // id
    private String id;
    // 用户id
    private String userId;
    // 目录父id
    private String cataloguePid;
    // 目录名称
    private String catalogueName;
    // 目录绝对路径
    private String cataloguePath;
    // 目录id绝对路径
    private String catalogueIdPath;
    // 描述
    private String cataDescribe;
    // 文件数量
    private Integer fileNumber;
    // 排序号
    private Integer orderNo;
    // 创建时间
    private Date createTime;
    // 有效标志(0-无效 1-有效)
    private String effetive;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCataloguePid() {
        return cataloguePid;
    }

    public void setCataloguePid(String cataloguePid) {
        this.cataloguePid = cataloguePid;
    }

    public String getCatalogueName() {
        return catalogueName;
    }

    public void setCatalogueName(String catalogueName) {
        this.catalogueName = catalogueName;
    }

    public String getCataloguePath() {
        return cataloguePath;
    }

    public void setCataloguePath(String cataloguePath) {
        this.cataloguePath = cataloguePath;
    }

    public String getCatalogueIdPath() {
        return catalogueIdPath;
    }

    public void setCatalogueIdPath(String catalogueIdPath) {
        this.catalogueIdPath = catalogueIdPath;
    }

    public String getCataDescribe() {
        return cataDescribe;
    }

    public void setCataDescribe(String describe) {
        this.cataDescribe = describe;
    }

    public Integer getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(Integer fileNumber) {
        this.fileNumber = fileNumber;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getEffetive() {
        return effetive;
    }

    public void setEffetive(String effetive) {
        this.effetive = effetive;
    }
}
