package com.shanqi.ssm.blog.interceptor;

import com.shanqi.ssm.blog.entity.User;
import com.shanqi.ssm.blog.enums.UserRole;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;


/**
 * @author shanqi
 */
@Component
public class AdminInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws IOException {
        // 这里可以根据session的用户来判断角色的权限，根据权限来转发不同的页面
        // 考虑使用 Redis 对这个进行改进，用户登录后，在 Redis 中存储用户信息的 Json 格式，key 为 Session 的 ID
        // 设置其存活时间，例如5分钟，当用户出现任何操作时，更新其过期时间
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect("/login");
            return false;
        }
        if (!Objects.equals(user.getUserRole(), UserRole.ADMIN.getValue())) {
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {

    }
}

