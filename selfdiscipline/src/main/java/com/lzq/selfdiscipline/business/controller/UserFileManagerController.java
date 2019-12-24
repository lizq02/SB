package com.lzq.selfdiscipline.business.controller;

import com.lzq.selfdiscipline.business.bean.MessageBean;
import com.lzq.selfdiscipline.business.bean.UserBean;
import com.lzq.selfdiscipline.business.bean.UserFileCatalogueBean;
import com.lzq.selfdiscipline.ta.constant.BusinessCode;
import com.lzq.selfdiscipline.ta.interceptors.LoginInterceptor;
import com.lzq.selfdiscipline.business.service.UserFileManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

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
        return "before/userFileManager";
    }

    /**
     * 用户文件管理:添加目录
     * @param catalogueId 父级id
     * @param catalogueName 父级名称
     * @param map
     * @return
     */
    @GetMapping("/userFileManagerController!getAddDirectoryPage.do")
    public String getAddDirectoryPage(String catalogueId, String catalogueName, Map map) {
        map.put("pid", catalogueId);
        map.put("pname", catalogueName);
        return "before/addDirectory";
    }

    /**
     * 用户文件管理:修改目录
     * @param catalogueId 目录id
     * @param map
     * @return
     */
    @GetMapping("/userFileManagerController!getUpdateDirectoryPage.do")
    public String getUpdateDirectoryPage(String catalogueId, Map map) {
        UserBean userBean = LoginInterceptor.getUserInfo();
        // 根据目录id 查询 用户目录信息
        map.put("directory", userFileManagerService.queryUserCatalogueByid(userBean.getId(), catalogueId).getData());
        return "before/updateDirectory";
    }

    /**
     * 新增文件
     * @return
     */
    @GetMapping("/userFileManagerController!getInsertFilePage.do")
    public String getInsertFilePage() {
        return "before/insertFile";
    }

    /**
     * 查询用户文件目录list
     *
     * @return
     */
    @RequestMapping("/userFileManagerController!queryUserCatalogues.do")
    @ResponseBody
    public MessageBean queryUserCatalogues() {
        UserBean userBean = LoginInterceptor.getUserInfo();
        return userFileManagerService.queryUserCatalogues(userBean.getId());
    }

    /**
     * 根据条件查询用户文件信息
     * @param catalogueId 目录id
     * @return
     */
    @RequestMapping("/userFileManagerController!queryFiles.do")
    @ResponseBody
    public MessageBean queryFiles(String catalogueId, Integer page, Integer pageSize) {
        UserBean userBean = LoginInterceptor.getUserInfo();
        return userFileManagerService.queryFiles(userBean.getId(), catalogueId, page, pageSize);
    }

    /**
     * 添加目录
     * @param bean
     * @return
     */
    @RequestMapping("/userFileManagerController!addDirectory.do")
    @ResponseBody
    public MessageBean addDirectory(UserFileCatalogueBean bean) {
        UserBean userBean = LoginInterceptor.getUserInfo();
        return userFileManagerService.addDirectory(bean, userBean);
    }

    /**
     * 修改目录
     * @param bean
     * @return
     */
    @RequestMapping("/userFileManagerController!updateDirectory.do")
    @ResponseBody
    public MessageBean updateDirectory(UserFileCatalogueBean bean) {
        UserBean userBean = LoginInterceptor.getUserInfo();
        MessageBean msg;
        try {
            msg =  userFileManagerService.updateDirectory(bean, userBean);
        } catch (Exception e) {
            msg = MessageBean.getInstance(BusinessCode.EXCEPTION.getCode(), e.getMessage(), void.class);
            e.printStackTrace();
        }
        return msg;
    }

    /**
     * 删除 目录
     * @return
     */
    @RequestMapping("/userFileManagerController!deleteDirectory.do")
    @ResponseBody
    public MessageBean deleteDirectory(String catalogueId) {
        UserBean userBean = LoginInterceptor.getUserInfo();
        MessageBean msg;
        try {
            return userFileManagerService.deleteDirectory(catalogueId, userBean);
        } catch (Exception e) {
            msg = MessageBean.getInstance(BusinessCode.EXCEPTION.getCode(), e.getMessage(), void.class);
            e.printStackTrace();
        }
        return msg;
    }

    /**
     * 文件上传
     * @param catalogueId
     * @param file
     * @return
     */
    @RequestMapping("/userFileManagerController!uploadFile.do")
    @ResponseBody
    public MessageBean uploadFile(String catalogueId, MultipartFile file) {
        MessageBean msg;
        try {
            return userFileManagerService.uploadFile(catalogueId, file);
        } catch (Exception e) {
            msg = MessageBean.getInstance(BusinessCode.EXCEPTION.getCode(), e.getMessage(), void.class);
            e.printStackTrace();
        }
        return msg;
    }

    /**
     * 删除文件
     * @return
     */
    @RequestMapping("userFileManagerController!deleteFileById.do")
    @ResponseBody
    public MessageBean deleteFileById(@RequestParam(value = "fileId", required = true) String fileId) {
        MessageBean msg;
        try {
            return userFileManagerService.deleteFileById(fileId);
        } catch (Exception e) {
            msg = MessageBean.getInstance(BusinessCode.EXCEPTION.getCode(), e.getMessage(), void.class);
            e.printStackTrace();
        }
        return msg;
    }
}
