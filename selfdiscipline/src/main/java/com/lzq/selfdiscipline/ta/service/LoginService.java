package com.lzq.selfdiscipline.ta.service;

import com.lzq.selfdiscipline.business.bean.UserBean;

public interface LoginService {
    /**
     * 根据登录账号id 查询用户信息
     * @param loginid
     * @return
     */
    UserBean queryUserByLoginid(String loginid);
}
