package com.lzq.selfdiscipline.controller;

import com.lzq.selfdiscipline.bean.MessageBean;
import com.lzq.selfdiscipline.service.UserFileManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

/**
 * 用户文件管理 controller
 */
@RestController
@RequestMapping("/userFileManager")
public class UserFileManagerController {

    @Autowired
    private UserFileManagerService userFileManagerService;

    /**
     * 查询用户文件目录list
     *
     * @return
     */
    @RequestMapping("/userFileManagerController!queryUserCatalogues.do")
    public MessageBean queryUserCatalogues() {
        return userFileManagerService.queryUserCatalogues("USER001");
    }

    /**
     * 根据条件查询用户文件信息
     * @param catalogueId 目录id
     * @return
     */
    @RequestMapping("/userFileManagerController!queryFiles.do")
    public MessageBean queryFiles(String catalogueId) {
        return userFileManagerService.queryFiles("USER001", catalogueId);
    }
}
