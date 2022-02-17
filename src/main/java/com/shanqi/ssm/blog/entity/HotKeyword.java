package com.shanqi.ssm.blog.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class HotKeyword implements Serializable {
    //这个实体类表示热点关键字，需要一个表进行记录每个关键字及其搜索次数
    //使用redis缓存每一天中的所有关键字及其搜索次数，这里采用zset较好
    //只记录100个关键字，使用LRU模式
    //之后每隔1小时对数据库中关键字的热度进行更新，只保留前20条数据留作下个小时的推荐关键字
    //同时清空zset，重新计数
    //每搜索一个关键字，便会查看是否

    private String keyword;

    private Integer count;

    public HotKeyword(String keyword, Integer count) {
        this.keyword = keyword;
        this.count = count;
    }
}
//create table hot_keywords(
//    hot_keyword_id int not null auto_incement COMMENT '关键词ID',
//    keyword varchar(50) default null COMMENT '关键词内容',
//    count int default 1 COMMENT '关键词出现次数',
//    primary key(`hot_keyword_id`)
//) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;
//BEGIN;
//insert into hot_keywords(keyword, count) values("测试",30);
//insert into hot_keywords(keyword, count) values("Spring",123);
//insert into hot_keywords(keyword, count) values("Java",1231);
//insert into hot_keywords(keyword, count) values("JVM",425);
//insert into hot_keywords(keyword, count) values("Cloud",563);
//insert into hot_keywords(keyword, count) values("Vue",346);
//insert into hot_keywords(keyword, count) values("虚拟机",426);
//insert into hot_keywords(keyword, count) values("SSM",566);
//insert into hot_keywords(keyword, count) values("SpringBoot",447);
//insert into hot_keywords(keyword, count) values("消息队列",798);
//insert into hot_keywords(keyword, count) values("Redis",456);
//insert into hot_keywords(keyword, count) values("数据结构",537);
//insert into hot_keywords(keyword, count) values("操作系统",678);
//COMMIT;