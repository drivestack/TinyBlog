package com.shanqi.ssm.blog.service.impl;

import com.shanqi.ssm.blog.entity.HotKeyword;
import com.shanqi.ssm.blog.mapper.HotKeywordMapper;
import com.shanqi.ssm.blog.service.HotKeywordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class HotKeywordServiceImpl implements HotKeywordService {
    @Autowired
    private HotKeywordMapper hotKeywordMapper;
    @Override
    public Integer countHotKeyword() {

        return hotKeywordMapper.countHotKeyword();
    }

    @Override
    public List<HotKeyword> getAllHotKeywords() {

        return hotKeywordMapper.getAllHotKeywords();
    }

    @Override
    public void updateHotKeyword(HotKeyword hotKeyword) {
        hotKeywordMapper.updateHotKeyword(hotKeyword);
    }

    @Override
    public void deleteHotKeyword(String keyword) {
        hotKeywordMapper.deleteHotKeyword(keyword);
    }
}
