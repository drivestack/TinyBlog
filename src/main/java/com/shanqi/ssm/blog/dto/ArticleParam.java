package com.shanqi.ssm.blog.dto;

import lombok.Data;

import java.util.List;

/**
 * @author 擅棋
 * @date 2022/2/11 下午3:56
 */
@Data
public class ArticleParam {

    private Integer articleId;

    private String articleTitle;

    private String articleContent;

    private Integer articleParentCategoryId;

    private Integer articleChildCategoryId;

    private Integer articleOrder;

    private Integer articleStatus;

    private String articleThumbnail;

    private List<Integer> articleTagIds;

}
