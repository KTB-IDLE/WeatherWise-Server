package com.idle.weather.auth.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        System.out.println("CorsFilter.doFilter");
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173"); // 클라이언트 도메인을 명시적으로 설정
        response.setHeader("Access-Control-Allow-Credentials", "true"); // 인증 정보를 포함한 요청을 허용
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS"); // 허용할 HTTP 메서드를 명시적으로 설정
        response.setHeader("Access-Control-Max-Age", "3600"); // 사전 요청(Preflight request) 결과를 캐싱하는 시간
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization"); // 요청에 허용할 헤더 목록
        response.setHeader("Access-Control-Expose-Headers", "Authorization"); // 클라이언트가 접근할 수 있도록 허용할 응답 헤더


        if("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        }else {
            chain.doFilter(req, res);
        }
    }

    @Override
    public void destroy() {

    }
}