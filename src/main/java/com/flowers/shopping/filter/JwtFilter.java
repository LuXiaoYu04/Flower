package com.flowers.shopping.filter;

import com.flowers.shopping.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = request.getHeader("token");
        String path = request.getServletPath();
        // 排除不需要拦截的路径
        if (path.startsWith("/login") ||
                path.startsWith("/register") ||
                path.startsWith("/send-verification-code") ||
                path.startsWith("/verify-code")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (token != null && !token.isBlank()) {
            try {
                Claims claims = JwtUtils.parseJWT(token);
                String username = claims.get("username", String.class);

                // 打印调试信息
                System.out.println("解析到用户名：" + username);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "无效的 Token");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

}
