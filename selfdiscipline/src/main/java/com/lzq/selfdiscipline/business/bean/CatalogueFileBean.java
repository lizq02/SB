package com.lzq.selfdiscipline.business.bean;

/**
 * 目录文件管理表
 */
public class CatalogueFileBean {
    // id
    private String id;
    // 用户文件目录id
    private String userFileCatalogueId;
    // 文件管理id
    private String fileManagerId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserFileCatalogueId() {
        return userFileCatalogueId;
    }

    public void setUserFileCatalogueId(String userFileCatalogueId) {
        this.userFileCatalogueId = userFileCatalogueId;
    }

    public String getFileManagerId() {
        return fileManagerId;
    }

    public void setFileManagerId(String fileManagerId) {
        this.fileManagerId = fileManagerId;
    }
}
