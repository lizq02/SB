package com.lzq.selfdiscipline.business.service.impl;

import com.lzq.selfdiscipline.business.Constants;
import com.lzq.selfdiscipline.business.SystemProperties;
import com.lzq.selfdiscipline.business.bean.*;
import com.lzq.selfdiscipline.business.util.FileUtil;
import com.lzq.selfdiscipline.ta.constant.BusinessCode;
import com.lzq.selfdiscipline.ta.constant.SysConstants;
import com.lzq.selfdiscipline.business.mapper.UserFileManagerMapper;
import com.lzq.selfdiscipline.business.service.UserFileManagerService;
import com.lzq.selfdiscipline.ta.interceptors.LoginInterceptor;
import com.lzq.selfdiscipline.ta.service.impl.BaseServiceImpl;
import com.lzq.selfdiscipline.business.util.ProjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class UserFileManagerServiceImpl extends BaseServiceImpl implements UserFileManagerService {
    private Logger logger = LoggerFactory.getLogger(UserFileManagerServiceImpl.class);

    @Autowired
    private UserFileManagerMapper userFileManagerMapper;

    @Override
    public MessageBean queryUserCatalogues(String userid) {
        MessageBean msg = MessageBean.getInstance(BusinessCode.SUCCESS.getCode(), BusinessCode.SUCCESS.getDescription(), List.class);
        // 根据 用户id 查询用户文件目录信息
        msg.setData(userFileManagerMapper.queryUserCatalogues(userid, SysConstants.EFFETIVE_1));
        return msg;
    }

    @Override
    public MessageBean<PageBean> queryFiles(String userid, String catalogueId, Integer page, Integer pageSize) {
        MessageBean<PageBean> msg = MessageBean.getInstance(BusinessCode.SUCCESS.getCode(), BusinessCode.SUCCESS.getDescription(), PageBean.class);
        List<String> catalogueIds = null;
        if (ProjectUtil.isNotEmpty(catalogueId)) {
            // 根据 用户id 查询用户文件目录信息
            List<Map<String, String>> list = userFileManagerMapper.queryUserCatalogues(userid, SysConstants.EFFETIVE_1);
            catalogueIds = list.stream().filter(map ->
                    Optional.ofNullable(map.get("catalogueIdPath")).orElse("").indexOf(catalogueId) > -1
            ).map(m -> m.get("id")).collect(Collectors.toList());
            if (catalogueIds == null || catalogueIds.size() == 0) {
                catalogueIds = null;
            }
        }
        page = (page == null || page < 1) ? 1 : page;
        pageSize = (pageSize == null || pageSize < 1) ? 15 : pageSize;
        Integer startPage = (page - 1) * pageSize;
        // 根据 用户id 查询用户文件目录信息
        List<Map> files = userFileManagerMapper.queryFiles(userid, catalogueIds, startPage, pageSize, SysConstants.EFFETIVE_1);
        if (files != null && files.size() > 0) {
            for (Map map : files) {
                if (map.get("filePreviewUrl") != null) {
                    map.put("filePreviewUrl", SystemProperties.virtualPathmap + File.separator + map.get("filePreviewUrl").toString());
                }
            }
        }
        Integer total = userFileManagerMapper.countQueryFiles(userid, catalogueIds, SysConstants.EFFETIVE_1);
        PageBean pageBean = new PageBean(page, pageSize, total, files);
        msg.setData(pageBean);
        return msg;
    }

    @Override
    public MessageBean addDirectory(UserFileCatalogueBean bean, UserBean userBean) {
        MessageBean msg = MessageBean.getInstance(BusinessCode.SUCCESS.getCode(), BusinessCode.SUCCESS.getDescription(), void.class);
        // 参数校验
        if (StringUtils.isEmpty(bean.getCataloguePid())) {
            msg.setCode(BusinessCode.FAILURE.getCode());
            msg.setMessage("请选择父目录");
            return msg;
        }
        if (StringUtils.isEmpty(bean.getCatalogueName())) {
            msg.setCode(BusinessCode.FAILURE.getCode());
            msg.setMessage("目录名称不能为空");
            return msg;
        } else if (bean.getCatalogueName().length() > 20) {
            msg.setCode(BusinessCode.FAILURE.getCode());
            msg.setMessage("目录名称最多输入20个字符");
            return msg;
        }
        // 父目录ID
        String cataloguePid = bean.getCataloguePid();
        UserFileCatalogueBean parentCata;
        if ("0".equals(cataloguePid)) {// 0 表示根目录
            parentCata = null;
        } else {
            parentCata = userFileManagerMapper.getUserCataloguesById(cataloguePid, SysConstants.EFFETIVE_1);
            if (parentCata == null || !parentCata.getUserId().equals(userBean.getId())) {
                msg.setCode(BusinessCode.FAILURE.getCode());
                msg.setMessage("该父目录不存在");
                return msg;
            }
        }
        String id = getSeq(Constants.SEQ);
        bean.setId(id);
        if (parentCata == null) {
            bean.setCataloguePath("/" + bean.getCatalogueName());
            bean.setCatalogueIdPath("/" + id);
        } else {
            bean.setCataloguePath(parentCata.getCataloguePath() + "/" + bean.getCatalogueName());
            bean.setCatalogueIdPath(parentCata.getCatalogueIdPath() + "/" + id);
        }
        // 判断级数
        if (bean.getCatalogueIdPath().split("/").length > 4) {
            msg.setCode(BusinessCode.FAILURE.getCode());
            msg.setMessage("当前系统最多支持4级目录");
            return msg;
        }
        bean.setUserId(userBean.getId());
        bean.setFileNumber(0);
        bean.setEffetive(SysConstants.EFFETIVE_1);
        bean.setCreateTime(new Date());
        userFileManagerMapper.insertDirectory(bean);
        return msg;
    }

    @Override
    public MessageBean<UserFileCatalogueBean> queryUserCatalogueByid(String userid, String id) {
        MessageBean msg = MessageBean.getInstance(BusinessCode.SUCCESS.getCode(), BusinessCode.SUCCESS.getDescription(), UserFileCatalogueBean.class);
        // 根据目录id 查询 用户目录信息
        msg.setData(userFileManagerMapper.queryUserCatalogueByid(userid, id, SysConstants.EFFETIVE_1));
        return msg;
    }

    @Override
    public MessageBean updateDirectory(UserFileCatalogueBean bean, UserBean userBean) throws Exception {
        MessageBean msg = MessageBean.getInstance(BusinessCode.SUCCESS.getCode(), BusinessCode.SUCCESS.getDescription(), void.class);
        // 参数校验
        if (StringUtils.isEmpty(bean.getCataloguePid())) {
            msg.setCode(BusinessCode.FAILURE.getCode());
            msg.setMessage("请选择父目录");
            return msg;
        }
        if (StringUtils.isEmpty(bean.getCatalogueName())) {
            msg.setCode(BusinessCode.FAILURE.getCode());
            msg.setMessage("目录名称不能为空");
            return msg;
        } else if (bean.getCatalogueName().length() > 20) {
            msg.setCode(BusinessCode.FAILURE.getCode());
            msg.setMessage("目录名称最多输入20个字符");
            return msg;
        }
        UserFileCatalogueBean userFileCatalogueBean = userFileManagerMapper.getUserCataloguesById(bean.getId(), SysConstants.EFFETIVE_1);
        if (userFileCatalogueBean == null || !userFileCatalogueBean.getUserId().equals(userBean.getId())) {
            msg.setCode(BusinessCode.FAILURE.getCode());
            msg.setMessage("修改失败，请刷新页面");
            return msg;
        }
        // 父目录ID
        String cataloguePid = bean.getCataloguePid();
        if (!StringUtils.isEmpty(cataloguePid) && cataloguePid.equals(bean.getId())) {
            msg.setCode(BusinessCode.FAILURE.getCode());
            msg.setMessage("父目录不能为当前修改目录");
            return msg;
        }
        UserFileCatalogueBean parentCata;
        if ("0".equals(cataloguePid)) {// 0 表示根目录
            parentCata = null;
        } else {
            parentCata = userFileManagerMapper.getUserCataloguesById(cataloguePid, SysConstants.EFFETIVE_1);
            if (parentCata == null || !parentCata.getUserId().equals(userBean.getId())) {
                msg.setCode(BusinessCode.FAILURE.getCode());
                msg.setMessage("该父目录不存在");
                return msg;
            } else if (parentCata.getCatalogueIdPath().indexOf(bean.getId()) > -1) {
                msg.setCode(BusinessCode.FAILURE.getCode());
                msg.setMessage("父目录不能为当前节点的子目录");
                return msg;
            }
        }
        if (parentCata == null) {
            bean.setCataloguePath("/" + bean.getCatalogueName());
            bean.setCatalogueIdPath("/" + bean.getId());
        } else {
            bean.setCataloguePath(parentCata.getCataloguePath() + "/" + bean.getCatalogueName());
            bean.setCatalogueIdPath(parentCata.getCatalogueIdPath() + "/" + bean.getId());
        }
        // 判断级数
        if (bean.getCatalogueIdPath().split("/").length > 4) {
            msg.setCode(BusinessCode.FAILURE.getCode());
            msg.setMessage("当前系统最多支持4级目录");
            return msg;
        }
        bean.setUserId(userBean.getId());
        if (userFileManagerMapper.updateDirectory(bean) != 1) {
            throw new Exception("修改失败");
        }
        return msg;
    }

    @Override
    public MessageBean deleteDirectory(String id, UserBean userBean) throws Exception {
        MessageBean msg = MessageBean.getInstance(BusinessCode.SUCCESS.getCode(), BusinessCode.SUCCESS.getDescription(), void.class);
        // 检查该 目录是否是父目录，如果是，不允许删除
        if (ProjectUtil.isNotEmpty(id)) {
            List<String> catalogueIds;
            // 根据 用户id 查询用户文件目录信息
            List<Map<String, String>> list = userFileManagerMapper.queryUserCatalogues(LoginInterceptor.getUserInfo().getId(), SysConstants.EFFETIVE_1);
            catalogueIds = list.stream().filter(map ->
                    Optional.ofNullable(map.get("catalogueIdPath"))
                            .orElse("")
                            .indexOf(id) > -1)
                    .map(m -> m.get("id"))
                    .filter(str -> !id.equals(str))
                    .collect(Collectors.toList());
            if (catalogueIds != null && catalogueIds.size() > 0) {
                msg.setCode(BusinessCode.FAILURE.getCode());
                msg.setMessage("该目录为父目录，不允许删除");
                return msg;
            }
        }
        // 检查该 目录下是否存在文件，如果存在文件，不允许删除
        if (userFileManagerMapper.countQueryFiles(userBean.getId(), Arrays.asList(id), SysConstants.EFFETIVE_1) > 0) {
            msg.setCode(BusinessCode.FAILURE.getCode());
            msg.setMessage("该目录下存在文件，不允许删除");
            return msg;
        }
        if (userFileManagerMapper.deleteDirectoryById(id, userBean.getId()) != 1) {
            throw new Exception("删除失败");
        }
        return msg;
    }

    @Override
    public MessageBean uploadFile(String catalogueId, MultipartFile file) throws Exception {
        UserBean userBean = LoginInterceptor.getUserInfo();
        MessageBean msg = MessageBean.getInstance(BusinessCode.SUCCESS.getCode(), BusinessCode.SUCCESS.getDescription(), void.class);
        if (StringUtils.isEmpty(catalogueId)) {
            msg.setCode(BusinessCode.FAILURE.getCode());
            msg.setMessage("请选择目录");
            return msg;
        } else if (userFileManagerMapper.queryUserCatalogueByid(userBean.getId(), catalogueId, SysConstants.EFFETIVE_1) == null) {
            msg.setCode(BusinessCode.FAILURE.getCode());
            msg.setMessage("选择目录不存在，请刷新页面");
            return msg;
        }
        if (file == null || file.isEmpty()) {
            msg.setCode(BusinessCode.FAILURE.getCode());
            msg.setMessage("请选择要上传的文件");
            return msg;
        }
        if (file.getSize() > 5 * 1024 * 1024) {
            msg.setCode(BusinessCode.FAILURE.getCode());
            msg.setMessage("最大只能上传5MB的文件");
            return msg;
        }
        String filename = file.getOriginalFilename();
        int index = filename.lastIndexOf(".");
        String filetype = filename.substring(index + 1).toLowerCase();
        if (FileUtil.list.indexOf(filetype) < 0) {
            msg.setCode(BusinessCode.FAILURE.getCode());
            msg.setMessage("只能上传" + FileUtil.list.toString() + "类型的文件");
            return msg;
        }
        // 文件存储相对路径
        String fileUrl = FileUtil.fileUpload(file);
        // 文件可预览相对路径
        String filePreviewUrl;
        if ("html".equals(filetype) || "htm".equals(filetype)) {
            filePreviewUrl = fileUrl;
        } else {
            filePreviewUrl = FileUtil.getFilePreviewUrl(fileUrl);
        }
        String fileManagerId = getSeq(Constants.SEQ);
        FileManagerBean bean = new FileManagerBean();
        bean.setId(fileManagerId);
        bean.setFileName(filename.substring(0, index));
        bean.setFileSize(file.getSize());
        bean.setFileType(filetype);
        bean.setFileUrl(fileUrl);
        bean.setFilePreviewUrl(filePreviewUrl);
        bean.setAddTime(new Date());
        // 新增 文件管理表
        userFileManagerMapper.insertFileManager(bean);

        CatalogueFileBean catalogueFileBean = new CatalogueFileBean();
        catalogueFileBean.setId(getSeq(Constants.SEQ));
        catalogueFileBean.setFileManagerId(fileManagerId);
        catalogueFileBean.setUserFileCatalogueId(catalogueId);
        // 新增 目录文件管理表
        userFileManagerMapper.insertCatalogueFile(catalogueFileBean);
        return msg;
    }

    @Override
    public MessageBean deleteFileById(String fileId) throws Exception {
        MessageBean msg = MessageBean.getInstance(BusinessCode.SUCCESS.getCode(), BusinessCode.SUCCESS.getDescription(), void.class);
        // 根据 文件id查询文件信息
        Map map = userFileManagerMapper.queryFileById(fileId, LoginInterceptor.getUserInfo().getId(), SysConstants.EFFETIVE_1);
        if (map == null){
            msg.setCode(BusinessCode.FAILURE.getCode());
            msg.setMessage("删除文件失败");
            throw new Exception("删除文件失败");
        }
        // 目录文件管理 id
        String catalogueFileID = map.get("catalogueFileID").toString();
        if (userFileManagerMapper.deleteFileManagerById(fileId) != 1 && userFileManagerMapper.deleteCatalogueFileById(catalogueFileID) != 1){
            msg.setCode(BusinessCode.FAILURE.getCode());
            msg.setMessage("删除文件失败");
            throw new Exception("删除文件失败");
        }
        // 删除文件
        Optional.ofNullable(map.get("fileUrl")).ifPresent(url -> FileUtil.deleteFile(url.toString()));
        Optional.ofNullable(map.get("filePreviewUrl")).ifPresent(url -> FileUtil.deleteFile(url.toString()));
        return msg;
    }
}
