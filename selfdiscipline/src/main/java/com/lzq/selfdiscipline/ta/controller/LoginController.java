package com.lzq.selfdiscipline.ta.controller;

import com.lzq.selfdiscipline.business.bean.MessageBean;
import com.lzq.selfdiscipline.business.bean.UserBean;
import com.lzq.selfdiscipline.business.util.RedisClusterSingleton;
import com.lzq.selfdiscipline.ta.constant.BusinessCode;
import com.lzq.selfdiscipline.ta.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisCluster;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 登录 controller
 */
@Controller
public class LoginController {
    private String COOKIE_NAME = "self_discipline_sso_cookie";
    @Autowired
    private LoginService loginService;

    /**
     * 登录页面
     * @param request
     * @return
     */
    @GetMapping("loginController.do")
    public String execute(HttpServletRequest request) {
        request.setAttribute("sessionId", request.getSession().getId());
        return "login";
    }

    /**
     * 注册页面
     * @param request
     * @return
     */
    @GetMapping("loginController!registerPage.do")
    public String registerPage(HttpServletRequest request) {
        request.setAttribute("sessionId", request.getSession().getId());
        return "register";
    }

    /**
     * 登录
     *
     * @param request
     * @return
     */
    @RequestMapping("loginController!login.do")
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        UserBean userBean = loginService.queryUserByLoginid(username);
        HttpSession session = request.getSession();
        if (userBean == null) {
            session.setAttribute("errormsg", "提示:用户名错误");
            response.sendRedirect("loginController.do");
            return;
        }
        // 加密:   md5(md5(md5(密码) + 登录账号) + 验证码)
        String checkStr = DigestUtils.md5DigestAsHex((userBean.getPassword() + userBean.getLoginid()).getBytes());
        checkStr = DigestUtils.md5DigestAsHex((checkStr + session.getId()).getBytes());
        // 获取加密后信息
        String i = request.getParameter("i");
        if (checkStr.equals(i)) {// 登录成功
            session.setAttribute("userBean" + session.getId(), userBean);
            session.setAttribute(COOKIE_NAME, checkStr);
            //新建Cookie
            Cookie cookie = new Cookie(COOKIE_NAME, checkStr);
            //设置Cookie路径
            cookie.setPath("/");
            response.addCookie(cookie);
            response.sendRedirect("userFileManager/userFileManagerController.do");
        } else {
            //新建Cookie
            Cookie cookie = new Cookie(COOKIE_NAME, "");
            //设置Cookie路径
            cookie.setPath("/");
            response.addCookie(cookie);
            session.setAttribute("errormsg", "提示:用户名密码错误");
            response.sendRedirect("loginController.do");
        }
    }

    /**
     * 注册
     * @param loginid 用户账号
     * @param username 用户名称
     * @param password 用户密码
     * @param password_ 用户密码
     * @param phonenumber 电话
     * @param checkCode 验证码
     */
    @PostMapping("loginController!register.do")
    @ResponseBody
    public MessageBean register(@RequestParam("loginid") String loginid, @RequestParam("username") String username,
                                @RequestParam("password") String password, @RequestParam("password_") String password_,
                                @RequestParam("phonenumber") String phonenumber, @RequestParam("checkCode") String checkCode) {
        MessageBean msg;
        try {
            msg =  loginService.register(loginid, username, password, password_, phonenumber, checkCode);
        } catch (Exception e) {
            msg = MessageBean.getInstance(BusinessCode.EXCEPTION.getCode(), e.getMessage(), void.class);
            e.printStackTrace();
        }
        return msg;
    }
}
