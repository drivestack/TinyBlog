package com.shanqi.ssm.blog.service;

import com.shanqi.ssm.blog.entity.HotKeyword;

import java.util.List;
import java.util.Set;

public interface HotKeywordService {
    /**
     * 获取热点关键词的总个数
     */
    Integer countHotKeyword();

    /**
     * 获取热点关键词列表
     */
    List<HotKeyword> getAllHotKeywords();

    /**
     * 更新某个HotKeyword
     */

    void updateHotKeyword(HotKeyword hotKeyword);

    /**
     * 删除某个HotKeyword
     */

    void deleteHotKeyword(String keyword);
}
