package com.shanqi.ssm.blog.mapper;

import com.shanqi.ssm.blog.entity.HotKeyword;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

@Mapper
public interface HotKeywordMapper {
    /**
     * 获取热点关键词总数
     * @return
     */
    Integer countHotKeyword();

    /**
     * 获取热点关键词列表
     * @return
     */
    List<HotKeyword> getAllHotKeywords();

    /**
     * 更新某个热点关键词
     * @param hotKeyword 待更新的关键词对象
     * @return
     */
    Integer updateHotKeyword(HotKeyword hotKeyword);

    /**
     * 删除热点关键词信息
     * @param keyword 待删除的关键词
     * @return
     */
    Integer deleteHotKeyword(String keyword);
}
