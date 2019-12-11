package com.lzq.selfdiscipline.ta.interceptors;

import com.lzq.selfdiscipline.business.bean.UserBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * 登录拦截器
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
    private String COOKIE_NAME = "self_discipline_sso_cookie";

    // 存储用户信息
    private static final ThreadLocal<UserBean> userContainer = new ThreadLocal<>();

    private Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // cookie校验
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            HttpSession session = request.getSession();
            String cookieValue = Optional.ofNullable(session.getAttribute(COOKIE_NAME)).orElse("").toString();
            for (Cookie cookie : cookies) {
                if (COOKIE_NAME.equals(cookie.getName()) && cookie.getValue().equals(cookieValue)) {
                    UserBean userBean = (UserBean) session.getAttribute("userBean" + session.getId());
                    if (userBean == null) {
                        return false;
                    }
                    userContainer.set(userBean);
                    return true;
                }
            }
        }
        response.sendRedirect("/loginController.do");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        userContainer.remove();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/";
        request.setAttribute("basePath", basePath);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public static UserBean getUserInfo() {
        return userContainer.get();
    }
}
