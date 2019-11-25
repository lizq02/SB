package com.lzq.selfdiscipline.controller;

import com.lzq.selfdiscipline.bean.MessageBean;
import com.lzq.selfdiscipline.service.UserFileManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 用户文件管理 controller
 */
@Controller
@RequestMapping("/userFileManager")
public class UserFileManagerController {

    @Autowired
    private UserFileManagerService userFileManagerService;

    /**
     * 用户文件管理
     *
     * @return
     */
    @GetMapping("/userFileManagerController.do")
    public String execute() {
        return "after/userFileManager";
    }

    /**
     * 查询用户文件目录list
     *
     * @return
     */
    @RequestMapping("/userFileManagerController!queryUserCatalogues.do")
    @ResponseBody
    public MessageBean queryUserCatalogues() {
        return userFileManagerService.queryUserCatalogues("USER001");
    }

    /**
     * 根据条件查询用户文件信息
     * @param catalogueId 目录id
     * @return
     */
    @RequestMapping("/userFileManagerController!queryFiles.do")
    @ResponseBody
    public MessageBean queryFiles(String catalogueId) {
        return userFileManagerService.queryFiles("USER001", catalogueId);
    }
}
