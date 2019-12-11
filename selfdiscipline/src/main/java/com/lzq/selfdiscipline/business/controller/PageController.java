package com.lzq.selfdiscipline.business.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 页面路径 controller
 */
@Controller
public class PageController {
    /**
     * 登录
     * @return
     */
    @GetMapping("/getLoginPage.do")
    public String getLoginPage() {
        return "login";
    }

}
