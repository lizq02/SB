package com.lzq.selfdiscipline.mapper;

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
     * @param effetive 有效标志(0-无效 1-有效)
     * @return
     */
    List<Map> queryFiles(@Param("userid") String userid, @Param("catalogueIds") List<String> catalogueIds, @Param("effetive") String effetive);
}
