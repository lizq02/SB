package com.lzq.selfdiscipline.ta.service.impl;

import com.lzq.selfdiscipline.ta.mapper.BaseMapper;
import com.lzq.selfdiscipline.ta.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseServiceImpl implements BaseService {
    @Autowired
    private BaseMapper baseMapper;

    @Override
    public String getSeq(String seqName) {
        return baseMapper.getSeq(seqName);
    }
}
