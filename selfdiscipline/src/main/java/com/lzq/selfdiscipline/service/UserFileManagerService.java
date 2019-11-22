package com.lzq.selfdiscipline.service;

import com.lzq.selfdiscipline.bean.MessageBean;

public interface UserFileManagerService {
    /**
     * 根据userid查询用户文件目录list
     * @param userid userid
     * @return
     */
    MessageBean queryUserCatalogues(String userid);

    /**
     * 根据条件查询用户文件信息
     * @param userid userid
     * @param catalogueId 目录id
     * @return
     */
    MessageBean queryFiles(String userid, String catalogueId);
}
