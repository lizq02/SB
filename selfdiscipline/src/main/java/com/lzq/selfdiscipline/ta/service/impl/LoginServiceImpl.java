package com.lzq.selfdiscipline.ta.service.impl;

import com.lzq.selfdiscipline.business.bean.MessageBean;
import com.lzq.selfdiscipline.business.bean.UserBean;
import com.lzq.selfdiscipline.ta.constant.BusinessCode;
import com.lzq.selfdiscipline.ta.constant.SysConstants;
import com.lzq.selfdiscipline.ta.mapper.LoginMapper;
import com.lzq.selfdiscipline.ta.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {
    @Autowired
    private LoginMapper loginMapper;

    @Override
    public UserBean queryUserByLoginid(String loginid) {
        return loginMapper.getUserByLoginid(loginid, SysConstants.EFFETIVE_1);
    }

    @Override
    public MessageBean register(String loginid, String username, String password, String password_, String phonenumber, String checkCode) throws Exception {
        MessageBean msg = registerCheckout(loginid, username, password, password_, phonenumber, checkCode);
        // 参数校验
        if (msg.getCode() != BusinessCode.SUCCESS.getCode()) {
            return msg;
        }
        // 检查登录账号是否已经存在
        if (loginMapper.getUserByLoginid(loginid, SysConstants.EFFETIVE_1) != null) {
            msg.setCode(BusinessCode.EXCEPTION.getCode());
            msg.setMessage("该登录账号已经存在");
            return msg;
        }
        // TODO 登录验证码校验
        // 新增 用户
        try {
            UserBean bean = new UserBean();
            bean.setId(loginid);
            bean.setUsername(username);
            bean.setLoginid(loginid);
            bean.setPassword(password);
            bean.setRegisterTime(new Date());
            bean.setEffetive(SysConstants.EFFETIVE_1);
            loginMapper.insertUser(bean);
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("注册失败");
        }
        return msg;
    }

    /**
     * 注册 参数校验
     *
     * @param loginid     用户账号
     * @param username    用户名称
     * @param password    用户密码
     * @param password_   用户密码
     * @param phonenumber 电话
     * @param checkCode   验证码
     * @return
     */
    private MessageBean registerCheckout(String loginid, String username, String password, String password_, String phonenumber, String checkCode) {
        MessageBean msg = MessageBean.getInstance(BusinessCode.SUCCESS.getCode(), BusinessCode.SUCCESS.getDescription(), void.class);
        if (StringUtils.isEmpty(loginid)) {
            msg.setCode(BusinessCode.FAILURE.getCode());
            msg.setMessage("登录账号不能为空!!!");
            return msg;
        }
        if (StringUtils.isEmpty(username)) {
            msg.setCode(BusinessCode.FAILURE.getCode());
            msg.setMessage("用户名称不能为空!!!");
            return msg;
        }
        if (StringUtils.isEmpty(password) || StringUtils.isEmpty(password_)) {
            msg.setCode(BusinessCode.FAILURE.getCode());
            msg.setMessage("密码不能为空!!!");
            return msg;
        } else if (!password.equals(password_)) {
            msg.setCode(BusinessCode.FAILURE.getCode());
            msg.setMessage("两次输入的密码不一样!!!");
            return msg;
        }
        if (StringUtils.isEmpty(phonenumber)) {
            msg.setCode(BusinessCode.FAILURE.getCode());
            msg.setMessage("手机号码不能为空!!!");
            return msg;
        }
        if (StringUtils.isEmpty(checkCode)) {
            msg.setCode(BusinessCode.FAILURE.getCode());
            msg.setMessage("验证码不能为空!!!");
            return msg;
        }
        return msg;
    }
}
