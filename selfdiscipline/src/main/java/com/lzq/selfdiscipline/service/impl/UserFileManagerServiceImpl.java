package com.lzq.selfdiscipline.service.impl;

import com.google.common.base.Splitter;
import com.lzq.selfdiscipline.bean.MessageBean;
import com.lzq.selfdiscipline.constant.SysConstants;
import com.lzq.selfdiscipline.mapper.UserFileManagerMapper;
import com.lzq.selfdiscipline.service.UserFileManagerService;
import com.lzq.selfdiscipline.util.ProjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserFileManagerServiceImpl implements UserFileManagerService {
    private Logger logger = LoggerFactory.getLogger(UserFileManagerServiceImpl.class);

    @Autowired
    private UserFileManagerMapper userFileManagerMapper;

    @Override
    public MessageBean queryUserCatalogues(String userid) {
        MessageBean msg = MessageBean.getInstance("0", "success");
        // 根据 用户id 查询用户文件目录信息
        msg.setData(userFileManagerMapper.queryUserCatalogues(userid, SysConstants.EFFETIVE_1));
        return msg;
    }

    @Override
    public MessageBean queryFiles(String userid, String catalogueId) {
        MessageBean msg = MessageBean.getInstance("0", "success");
        List<String> catalogueIds = null;
        if (ProjectUtil.isNotEmpty(catalogueId)) {
            // 根据 用户id 查询用户文件目录信息
            List<Map<String, String>> list = userFileManagerMapper.queryUserCatalogues(userid, SysConstants.EFFETIVE_1);
            catalogueIds = list.stream().filter(map ->
                    Optional.ofNullable(map.get("catalogueIdPath")).orElse("").indexOf(catalogueId) > -1
            ).map(m -> m.get("id")).collect(Collectors.toList());
            if (catalogueIds == null || catalogueIds.size() == 0){
                catalogueIds = null;
            }
        }
        // 根据 用户id 查询用户文件目录信息
        msg.setData(userFileManagerMapper.queryFiles(userid, catalogueIds, SysConstants.EFFETIVE_1));
        return msg;
    }
}
