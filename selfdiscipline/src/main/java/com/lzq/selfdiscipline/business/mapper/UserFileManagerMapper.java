package com.lzq.selfdiscipline.business.mapper;

import com.lzq.selfdiscipline.business.bean.CatalogueFileBean;
import com.lzq.selfdiscipline.business.bean.FileManagerBean;
import com.lzq.selfdiscipline.business.bean.UserFileCatalogueBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserFileManagerMapper {

    /**
     * 根据 用户id 查询用户文件目录信息
     * @param userid
     * @param effetive 有效标志(0-无效 1-有效)
     * @return
     */
    List<Map<String, String>> queryUserCatalogues(@Param("userid") String userid, @Param("effetive") String effetive);

    /**
     * 根据条件查询用户文件信息
     * @param userid
     * @param catalogueIds 用户目录id 数组
     * @param startPage 开始页码
     * @param pageSize 每页条数
     * @param effetive 有效标志(0-无效 1-有效)
     * @return
     */
    List<Map> queryFiles(@Param("userid") String userid, @Param("catalogueIds") List<String> catalogueIds,
                         @Param("startPage") Integer startPage, @Param("pageSize") Integer pageSize,
                         @Param("effetive") String effetive);

    /**
     * 根据 统计 条件查询用户文件信息
     * @param userid
     * @param catalogueIds 用户目录id 数组
     * @param effetive 有效标志(0-无效 1-有效)
     * @return
     */
    Integer countQueryFiles(@Param("userid") String userid, @Param("catalogueIds") List<String> catalogueIds,
                         @Param("effetive") String effetive);

    /**
     * 根据id 查询 用户文件目录
     * @param id
     * @return
     */
    UserFileCatalogueBean getUserCataloguesById(@Param("id") String id, @Param("effetive") String effetive);

    /**
     * 新增 用户文件目录
     * @param bean
     * @return
     */
    Integer insertDirectory(UserFileCatalogueBean bean);

    /**
     *  根据目录id 查询 用户目录信息
     * @param userid
     * @param id
     * @param effetive
     * @return
     */
    UserFileCatalogueBean queryUserCatalogueByid(@Param("userid") String userid, @Param("id") String id, @Param("effetive") String effetive);

    /**
     * 修改 用户文件目录
     * @param bean
     * @return
     */
    Integer updateDirectory(UserFileCatalogueBean bean);

    /**
     * 根据id 删除用户文件目录
     * @param id id
     * @param userid userid
     * @return
     */
    Integer deleteDirectoryById(@Param("id") String id, @Param("userid") String userid);

    /**
     * 新增 文件管理表
     * @param bean
     * @return
     */
    Integer insertFileManager(FileManagerBean bean);

    /**
     * 新增 目录文件管理表
     * @param bean
     * @return
     */
    Integer insertCatalogueFile(CatalogueFileBean bean);

    /**
     * 根据 文件id查询文件信息
     * @param fileId
     * @param userid
     * @param effetive
     * @return
     */
    Map queryFileById(@Param("fileId") String fileId, @Param("userid") String userid, @Param("effetive") String effetive);

    /**
     * 根据id 删除 文件管理表
     * @param id)
     * @return
     */
    Integer deleteFileManagerById(String id);

    /**
     * 根据id 删除 目录文件管理表
     * @param id)
     * @return
     */
    Integer deleteCatalogueFileById(String id);
}
