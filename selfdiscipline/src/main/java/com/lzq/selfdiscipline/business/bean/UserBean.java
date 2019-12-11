package com.lzq.selfdiscipline.business.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户 bean
 */
public class UserBean implements Serializable {
    private String id;
    // 用户名称
    private String username;
    // 用户账号
    private String loginid;
    // 用户密码
    private String password;
    // 注册时间
    private Date registerTime;
    // 有效标志(0-无效 1-有效)
    private String effetive;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLoginid() {
        return loginid;
    }

    public void setLoginid(String loginid) {
        this.loginid = loginid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public String getEffetive() {
        return effetive;
    }

    public void setEffetive(String effetive) {
        this.effetive = effetive;
    }
}
