package com.lzq.selfdiscipline.ta.mapper;

import org.springframework.stereotype.Repository;

@Repository
public interface BaseMapper {
    /**
     * 根据 序列名称获取序列
     * @param seqName 序列名称
     * @return
     */
    String getSeq(String seqName);
}
