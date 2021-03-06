<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="rapid" uri="http://www.rapid-framework.org.cn/rapid" %>


<rapid:override name="breadcrumb">
    <nav class="breadcrumb">
        <div class="bull"><i class="fa fa-volume-up"></i></div>
        <div id="scrolldiv">
            <div class="scrolltext">
                <ul style="margin-top: 0px;">
                    <c:forEach items="${noticeList}" var="n">
                        <li class="scrolltext-title">
                            <a href="/notice/${n.noticeId}" rel="bookmark">${n.noticeTitle}</a>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </div>
    </nav>
</rapid:override>

<rapid:override name="left">
    <div id="primary" class="content-area">

        <form method="get" id="searchform" action="/search" accept-charset="UTF-8">
            <scan>
                <input type="hidden" name="keywords" value="null"/>
                sorted by:
                <select name="orderRule">
                    <option value="null">null</option>
                    <option value="article_update_time DESC">Newest first</option>
                    <option value="article_update_time">Oldest first</option>
                    <option value="article_view_count DESC">Most views</option>
                    <option value="article_comment_count DESC">Most comments</option>
                </select>
            </scan>
            <button type="submit" id="searchsubmit">ιζ°ζεΊ</button>
        </form>

        <main id="main" class="site-main" role="main">
            <c:forEach items="${pageInfo.list}" var="a">

                <article class="post type-post">

                    <figure class="thumbnail">
                        <a href="/article/${a.articleId}">
                            <img width="280" height="210"
                                 src="${a.articleThumbnail}"
                                 class="attachment-content size-content wp-post-image"
                                 alt="${a.articleTitle}">
                        </a>
                        <span class="cat">
                                <a href="/category/${a.categoryList[a.categoryList.size()-1].categoryId}">
                                        ${a.categoryList[a.categoryList.size()-1].categoryName}
                                </a>
                            </span>
                    </figure>

                    <header class="entry-header">
                        <h2 class="entry-title">
                            <a href="/article/${a.articleId}"
                               rel="bookmark">
                                    ${a.articleTitle}
                            </a>
                        </h2>
                    </header>

                    <div class="entry-content">
                        <div class="archive-content">
                                ${a.articleSummary}...
                        </div>
                        <span class="title-l"></span>
                        <span class="new-icon">
                            <c:choose>
                                <c:when test="${a.articleStatus == 2}">
                                    <i class="fa fa-bookmark-o"></i>
                                </c:when>
                                <c:otherwise>
                                    <jsp:useBean id="nowDate" class="java.util.Date"/>
                                    <c:set var="interval"
                                           value="${nowDate.time - a.articleCreateTime.time}"/><%--ζΆι΄ε·?ζ―«η§ζ°--%>
                                    <fmt:formatNumber value="${interval/1000/60/60/24}" pattern="#0"
                                                      var="days"/>
                                    <c:if test="${days <= 7}">NEW</c:if>
                                </c:otherwise>
                            </c:choose>
                        </span>
                        <span class="entry-meta">
                            <span class="date">
                                <fmt:formatDate value="${a.articleCreateTime}" pattern="yyyyεΉ΄MMζddζ₯"/>
                            &nbsp;&nbsp;
                            </span>
                            <span class="views">
                                <i class="fa fa-eye"></i>
                                    ${a.articleViewCount} views
                            </span>
                            <span class="comment">&nbsp;&nbsp;
                                <a href="/article/${a.articleId}#comments" rel="external nofollow">
                                  <i class="fa fa-comment-o"></i>
                                    <c:choose>
                                        <c:when test="${a.articleCommentCount == 0}">
                                            εθ‘¨θ―θ?Ί
                                        </c:when>
                                        <c:otherwise>
                                            ${a.articleCommentCount}
                                        </c:otherwise>
                                    </c:choose>

                                </a>
                            </span>
                        </span>
                        <div class="clear"></div>
                    </div><!-- .entry-content -->

                    <span class="entry-more">
                        <a href="/article/${a.articleId}"
                           rel="bookmark">
                            ιθ―»ε¨ζ
                        </a>
                    </span>
                </article>
            </c:forEach>
        </main>
        <%@ include file="Public/part/paging.jsp" %>

    </div>
</rapid:override>
<%--ε·¦δΎ§εΊε end--%>

<%--δΎ§θΎΉζ  start--%>
<rapid:override name="right">
    <%@include file="Public/part/sidebar-2.jsp" %>
</rapid:override>
<%--δΎ§θΎΉζ  end--%>

<%--εζιΎζ₯ start--%>
<rapid:override name="link">
    <div class="links-box">
        <div id="links">
            <c:forEach items="${linkList}" var="l">
                <ul class="lx7">
                    <li class="link-f link-name">
                        <a href="${l.linkUrl}" target="_blank">
                                ${l.linkName}
                        </a>
                    </li>
                </ul>
            </c:forEach>
            <div class="clear"></div>
        </div>
    </div>
</rapid:override>
<%--εζιΎζ₯ end--%>

<%@ include file="Public/framework.jsp" %>