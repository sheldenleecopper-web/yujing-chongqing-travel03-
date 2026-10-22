package com.yujing.config;

import com.yujing.common.JwtUtil;
import com.yujing.common.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String authHeader = request.getHeader("Authorization");
        String uri = request.getRequestURI();
        boolean isPublic = isPublicPath(uri);
        boolean isAdmin = uri.startsWith("/api/admin/");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String token = authHeader.substring(7);
                Long userId = jwtUtil.getUserId(token);
                String role = jwtUtil.getRole(token);
                UserContext.setUserId(userId);
                UserContext.setRole(role);
                // 管理类接口要求 ADMIN 角色
                if (isAdmin && !"ADMIN".equals(role)) {
                    response.setStatus(403);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"code\":403,\"message\":\"无管理员权限\"}");
                    return false;
                }
                return true;
            } catch (Exception e) {
                if (isPublic) {
                    return true;
                }
                response.setStatus(401);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":401,\"message\":\"登录已过期\"}");
                return false;
            }
        }

        // 未携带 token
        if (isPublic) {
            return true;
        }
        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":401,\"message\":\"未登录\"}");
        return false;
    }

    /**
     * 公开路径：无需登录即可访问，但若携带有效 token 则解析用户身份（用于浏览记录等）
     */
    private boolean isPublicPath(String uri) {
        return uri.startsWith("/api/spot/")
                || uri.startsWith("/api/heritage/")
                || uri.equals("/api/recommend/home");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.clear();
    }
}
