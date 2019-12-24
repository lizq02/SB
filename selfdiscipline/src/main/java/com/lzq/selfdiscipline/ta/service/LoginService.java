package com.lzq.selfdiscipline.ta.service;

import com.lzq.selfdiscipline.business.bean.MessageBean;
import com.lzq.selfdiscipline.business.bean.UserBean;

public interface LoginService {
    /**
     * 根据登录账号id 查询用户信息
     * @param loginid
     * @return
     */
    UserBean queryUserByLoginid(String loginid);

    /**
     * 注册
     * @param loginid 用户账号
     * @param username 用户名称
     * @param password 用户密码
     * @param password_ 用户密码
     * @param phonenumber 电话
     * @param checkCode 验证码
     * @return
     */
    MessageBean register(String loginid, String username, String password,
                         String password_, String phonenumber, String checkCode) throws Exception;
}
