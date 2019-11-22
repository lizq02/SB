package com.lzq.selfdiscipline.controller;

import com.lzq.selfdiscipline.service.UserFileManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

/**
 * 页面路径 controller
 */
@Controller
public class PageController {
    @GetMapping("/getIndex.do")
    public String getIndex() {
        return "index";
    }

    @GetMapping("/test.do")
    public String getTestPage() {
        return "/test";
    }

    /**
     * 登录
     *
     * @return
     */
    @GetMapping("/getLoginPage.do")
    public String getLoginPage() {
        return "login";
    }

    /**
     * 用户文件管理
     *
     * @return
     */
    @GetMapping("/getUserFileManagerPage.do")
    public String getUserFileManagerPage() {
        return "after/userFileManager";
    }
}
