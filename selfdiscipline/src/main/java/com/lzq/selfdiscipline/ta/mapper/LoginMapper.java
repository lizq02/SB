package com.lzq.selfdiscipline.ta.mapper;

import com.lzq.selfdiscipline.business.bean.UserBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginMapper {
    /**
     * 根据登录账号id 获取用户信息
     * @param loginid
     * @return
     */
    UserBean getUserByLoginid(@Param("loginid") String loginid, @Param("effetive") String effetive);
}
