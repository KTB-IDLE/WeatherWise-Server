package com.idle.weather.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.idle.weather.exception.ErrorResponse;
import com.idle.weather.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(final HttpServletRequest request,
                       final HttpServletResponse response,
                       final AccessDeniedException accessDeniedException) throws IOException {
        // ErrorResponse 객체 생성
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.ACCESS_DENIED_ERROR);

        // 응답 헤더 설정
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(ErrorCode.ACCESS_DENIED_ERROR.getStatus().value());

        // ErrorResponse 객체를 JSON으로 변환하여 응답 본문에 작성
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
    }
}
