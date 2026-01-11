package com.api_gateway.config;


import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

@Component
public class JwtAuthenticationFilter implements Filter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();
        String method = req.getMethod();

        System.out.println(">> PATH: " + path);
        System.out.println(">> METHOD: " + method);

        boolean protectedRoutePOST = (path.startsWith("/api/peliculas") || path.startsWith("/api/actores") || path.startsWith("/api/critica"))
                && method.equals("POST");

        boolean protectedRouteGET = (path.startsWith("/api/critica/usuario/") || path.startsWith("/api/usuario") || path.equals("/api/critica") || path.startsWith("/api/critica/buscar")) && method.equals("GET");

        boolean protectedRoutePUT = (path.startsWith("/api/critica/") || path.startsWith("/api/actores/") || path.startsWith("/api/usuario") || path.startsWith("/api/peliculas/") ) && method.equals("PUT");

        boolean protectedRouteDELETE = (path.startsWith("/api/critica") || path.startsWith("/api/actores/") || path.startsWith("/api/usuario") || path.startsWith("/api/peliculas/")) && method.equals("DELETE");



        if (!(protectedRoutePOST || protectedRouteGET || protectedRoutePUT || protectedRouteDELETE)) {
            chain.doFilter(request, response);
            return;
        }

        String token = null;

        // Extraer token del header Authorization
        String authHeader = req.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        // Si no está en header, buscar en cookies
        if (token == null && req.getCookies() != null) {
            for (Cookie cookie : req.getCookies()) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token == null) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token no proporcionado");
            return;
        }

        if (!jwtUtil.isTokenValid(token)) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
            return;
        }

        Claims claims = jwtUtil.getClaims(token);
        request.setAttribute("claims", claims);

        System.out.println("Token JWT extraído: " + token);
        System.out.println("Reinyectando Authorization header como Bearer");

        // Aquí creamos un wrapper que agrega el header Authorization
        String finalToken = token;

        HttpServletRequestWrapper wrappedRequest = new HttpServletRequestWrapper(req) {
            @Override
            public String getHeader(String name) {
                if ("Authorization".equalsIgnoreCase(name)) {
                    return "Bearer " + finalToken;
                }
                return super.getHeader(name);
            }

            @Override
            public Enumeration<String> getHeaders(String name) {
                if ("Authorization".equalsIgnoreCase(name)) {
                    return Collections.enumeration(List.of("Bearer " + finalToken));
                }
                return super.getHeaders(name);
            }

            @Override
            public Enumeration<String> getHeaderNames() {
                List<String> names = Collections.list(super.getHeaderNames());
                if (!names.contains("Authorization")) {
                    names.add("Authorization");
                }
                return Collections.enumeration(names);
            }
        };

        // Pasamos el request modificado con el header inyectado
        chain.doFilter(wrappedRequest, response);
    }
}
