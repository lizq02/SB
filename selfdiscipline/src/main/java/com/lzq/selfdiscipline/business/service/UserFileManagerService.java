package com.lzq.selfdiscipline.business.service;

import com.lzq.selfdiscipline.business.bean.MessageBean;
import com.lzq.selfdiscipline.business.bean.PageBean;
import com.lzq.selfdiscipline.business.bean.UserBean;
import com.lzq.selfdiscipline.business.bean.UserFileCatalogueBean;
import org.springframework.web.multipart.MultipartFile;

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
     * @param page 页码
     * @param pageSize 每页条数
     * @return
     */
    MessageBean<PageBean> queryFiles(String userid, String catalogueId, Integer page, Integer pageSize);

    /**
     * 添加 用户文件目录
     * @param bean 用户文件目录bean
     * @param userBean 用户bean
     * @return
     */
    MessageBean addDirectory(UserFileCatalogueBean bean, UserBean userBean);

    /**
     * 根据目录id 查询 用户目录信息
     * @param userid userid
     * @param id
     * @return
     */
    MessageBean<UserFileCatalogueBean> queryUserCatalogueByid(String userid, String id);

    /**
     * 修改 用户文件目录
     * @param bean 用户文件目录bean
     * @param userBean 用户bean
     * @return
     */
    MessageBean updateDirectory(UserFileCatalogueBean bean, UserBean userBean) throws Exception;

    /**
     * 根据id 删除用户目录
     * @param id
     * @param userBean
     * @return
     */
    MessageBean deleteDirectory(String id, UserBean userBean) throws Exception;

    /**
     * 文件上传
     * @param catalogueId 用户文件目录id
     * @param file 文件
     * @return
     */
    MessageBean uploadFile(String catalogueId, MultipartFile file) throws Exception;

    /**
     * 根据id 删除文件
     * @param fileId
     * @return
     * @throws Exception
     */
    MessageBean deleteFileById(String fileId) throws Exception;
}
