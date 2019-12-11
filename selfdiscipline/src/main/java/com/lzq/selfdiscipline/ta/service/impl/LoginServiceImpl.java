package com.lzq.selfdiscipline.ta.service.impl;

import com.lzq.selfdiscipline.business.bean.UserBean;
import com.lzq.selfdiscipline.ta.constant.SysConstants;
import com.lzq.selfdiscipline.ta.mapper.LoginMapper;
import com.lzq.selfdiscipline.ta.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {
    @Autowired
    private LoginMapper loginMapper;

    @Override
    public UserBean queryUserByLoginid(String loginid) {
        return loginMapper.getUserByLoginid(loginid, SysConstants.EFFETIVE_1);
    }
}
