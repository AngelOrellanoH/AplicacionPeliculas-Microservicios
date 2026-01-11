package com.microservicio_frontend.security;


import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;


public class JwtSessionFilter implements Filter {
    private final JwtUtil jwtUtil;

    public JwtSessionFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String token = null;

        if (req.getCookies() != null) {
            for (Cookie cookie : req.getCookies()) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token != null && jwtUtil.isTokenValid(token)) {
            req.setAttribute("username", jwtUtil.getUsername(token));
            req.setAttribute("role", jwtUtil.getRole(token));
        }

        chain.doFilter(request, response);
    }
}
