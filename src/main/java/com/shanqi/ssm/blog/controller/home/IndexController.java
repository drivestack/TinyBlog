package com.shanqi.ssm.blog.controller.home;

import com.github.pagehelper.PageInfo;
import com.shanqi.ssm.blog.entity.Link;

import com.shanqi.ssm.blog.enums.ArticleStatus;
import com.shanqi.ssm.blog.enums.LinkStatus;
import com.shanqi.ssm.blog.enums.NoticeStatus;

import com.shanqi.ssm.blog.entity.*;
import com.shanqi.ssm.blog.service.*;
import com.shanqi.ssm.blog.entity.*;
import com.shanqi.ssm.blog.service.*;
import org.checkerframework.framework.qual.RequiresQualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * 用户的controller
 *
 * @author 擅棋
 * @date 2022/1/25
 */
@Controller
public class IndexController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private LinkService linkService;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private TagService tagService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(value = {"/", "/article"})
    public String index(@RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                        @RequestParam(required = false, value = "orderRule") String orderRule,
                        @RequestParam(required = false, defaultValue = "10") Integer pageSize, Model model) {
        HashMap<String, Object> criteria = new HashMap<>(1);
        criteria.put("status", ArticleStatus.PUBLISH.getValue());
        criteria.put("orderRule", orderRule);
        //文章列表
        PageInfo<Article> articleList = articleService.pageArticle(pageIndex, pageSize, criteria);
        model.addAttribute("pageInfo", articleList);

        //公告
        List<Notice> noticeList = noticeService.listNotice(NoticeStatus.NORMAL.getValue());
        model.addAttribute("noticeList", noticeList);
        //友情链接
        List<Link> linkList = linkService.listLink(LinkStatus.NORMAL.getValue());
        model.addAttribute("linkList", linkList);

        //侧边栏显示
        //标签列表显示
        List<Tag> allTagList = tagService.listTag();
        model.addAttribute("allTagList", allTagList);
        //最新评论
        List<Comment> recentCommentList = commentService.listRecentComment(null, 10);
        model.addAttribute("recentCommentList", recentCommentList);
        model.addAttribute("pageUrlPrefix", "/article?pageIndex");
        return "Home/index";
    }

    @RequestMapping(value = "/search")
    public String search(
            @RequestParam(value = "keywords", required = false) String keywords,
            @RequestParam(value = "orderRule", defaultValue = "null", required = false) String orderRule,
            @RequestParam(required = false, defaultValue = "1") Integer pageIndex,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize, Model model) {

        //侧边栏显示
        //标签列表显示
        List<Tag> allTagList = tagService.listTag();
        model.addAttribute("allTagList", allTagList);
        //获得随机文章,底层使用 order by Rand()
        List<Article> randomArticleList = articleService.listRandomArticle(8);
        model.addAttribute("randomArticleList", randomArticleList);
        //获得热评文章
        List<Article> mostCommentArticleList = articleService.listArticleByCommentCount(8);
        model.addAttribute("mostCommentArticleList", mostCommentArticleList);
        //最新评论
        List<Comment> recentCommentList = commentService.listRecentComment(null, 10);
        model.addAttribute("recentCommentList", recentCommentList);
        // 这里修改了一下,否则在搜索结果超过一页时无法跳转到第二页
        model.addAttribute("pageUrlPrefix", "/search?keywords="+keywords+"&orderRule="+orderRule+"&pageIndex");

        //文章列表
        HashMap<String, Object> criteria = new HashMap<>(2);
        criteria.put("status", ArticleStatus.PUBLISH.getValue());

        //使用缓存记录热点关键字
        // 首先热词在redis中采用zset进行存储，内部是key:value的形式
        // 从数据库中读取时就不读id了，只读关键词keyword及其搜索次数count

        //  查找keywords在zSet内的排名
        Object exist = redisTemplate.boundZSetOps("zSet").rank(keywords);
        // 缓存中不存在该关键词则创建，并给定初始分值为1
        if(exist == null){
            redisTemplate.boundZSetOps("zSet").add(keywords, 1);
            System.out.println("add");
        }else{
            // 否则增加其分数，增量为1
            redisTemplate.boundZSetOps("zSet").incrementScore(keywords,1);
        }

//        Set<Object> keywordSet=redisTemplate.boundZSetOps("zSet").rangeByScore(0,100);
//        keywordSet.forEach(v -> System.out.printf("%s,",v.toString()));
//        System.out.printf("\n");
        //清空zSet中所有元素
        //redisTemplate.boundZSetOps("zSet").removeRange(0,-1);

        criteria.put("keywords", keywords);
        criteria.put("orderRule", orderRule);
        PageInfo<Article> articlePageInfo = articleService.pageArticle(pageIndex, pageSize, criteria);
        model.addAttribute("pageInfo", articlePageInfo);


        return "Home/Page/search";
    }

    @RequestMapping("/404")
    public String NotFound(@RequestParam(required = false) String message, Model model) {
        model.addAttribute("message", message);
        return "Home/Error/404";
    }


    @RequestMapping("/403")
    public String Page403(@RequestParam(required = false) String message, Model model) {
        model.addAttribute("message", message);
        return "Home/Error/403";
    }

    @RequestMapping("/500")
    public String ServerError(@RequestParam(required = false) String message, Model model) {
        model.addAttribute("message", message);
        return "Home/Error/500";
    }


}


//梳理一下步骤
//首先在数据库中有关键词信息表

//当第一次进行检索时，缓存与数据库中什么也没有，因此需要将检索的关键词进行写入redis的zSet中，并设置一定的过期时间
    //采用LRU缓存策略，删除最近最少使用的关键字
//之后每次进行检索，缓存中的关键词及频率需要进行更新或增量


//每次服务器重新启动时或者每天的0点，将数据库中的热点词加入到缓存中，之后不会再访问数据库
//运行中间隔一段时间，将redis中的数据进行持久化操作，可以是间隔几个小时。保存到mysql数据库中。



