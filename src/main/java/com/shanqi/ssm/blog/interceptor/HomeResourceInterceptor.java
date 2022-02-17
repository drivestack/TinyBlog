package com.shanqi.ssm.blog.interceptor;

import com.shanqi.ssm.blog.entity.*;

import com.shanqi.ssm.blog.enums.ArticleStatus;
import com.shanqi.ssm.blog.enums.LinkStatus;

import com.shanqi.ssm.blog.service.*;
import com.shanqi.ssm.blog.service.*;
import org.aspectj.lang.annotation.AfterThrowing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author shanqi
 */
@Component
public class HomeResourceInterceptor implements HandlerInterceptor {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    @Autowired
    private LinkService linkService;

    @Autowired
    private OptionsService optionsService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private HotKeywordService hotKeywordService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 在请求处理之前执行，该方法主要是用于准备资源数据的，然后可以把它们当做请求属性放到WebRequest中
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws IOException {

        // 菜单显示
        List<Menu> menuList = menuService.listMenu();
        request.setAttribute("menuList", menuList);

        List<Category> categoryList = categoryService.listCategory();
        request.setAttribute("allCategoryList", categoryList);

        List<HotKeyword> keywordsList = null;


        //尝试读取缓存中的热点关键字集合
        if(redisTemplate.boundZSetOps("zSet").size() == 0){
            //缓存中一个也没有则从数据库中读取，注意，这里做了限制，只能读取前20个热度最高的关键词
            keywordsList = hotKeywordService.getAllHotKeywords();
            System.out.println("read from mysql");
        }else{
            //否则从缓存中读取热点关键词信息
            //这里首先将redis中所有的关键词及其频率先取出，用TypedTuple进行包装
            Set<ZSetOperations.TypedTuple> tupleSet =
                    redisTemplate.boundZSetOps("zSet").rangeByScoreWithScores(0,Integer.MAX_VALUE);
            //之后将其封装为一个list，便于将其加入session域中在页面显示
            for(ZSetOperations.TypedTuple tuple : tupleSet){
                keywordsList.add(new HotKeyword((String)tuple.getValue(),tuple.getScore().intValue()));
            }
        }

        request.setAttribute("allHotKeywords", keywordsList);
        //获得网站概况
        List<String> siteBasicStatistics = new ArrayList<String>();
        siteBasicStatistics.add(articleService.countArticle(ArticleStatus.PUBLISH.getValue()) + "");
        siteBasicStatistics.add(articleService.countArticleComment() + "");
        siteBasicStatistics.add(categoryService.countCategory() + "");
        siteBasicStatistics.add(tagService.countTag() + "");
        siteBasicStatistics.add(linkService.countLink(LinkStatus.NORMAL.getValue()) + "");
        siteBasicStatistics.add(articleService.countArticleView() + "");
        request.setAttribute("siteBasicStatistics", siteBasicStatistics);
        //最后更新的文章
        Article lastUpdateArticle = articleService.getLastUpdateArticle();
        request.setAttribute("lastUpdateArticle", lastUpdateArticle);

        //页脚显示
        //博客基本信息显示(Options)
        Options options = optionsService.getOptions();
        request.setAttribute("options", options);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView)  {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e)  {

    }
}