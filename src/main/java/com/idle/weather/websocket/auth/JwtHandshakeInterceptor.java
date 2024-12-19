package com.idle.weather.websocket.auth;

import com.idle.weather.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler webSocketHandler,
            Map<String, Object> attributes) throws Exception {
        HttpHeaders headers = request.getHeaders();
        String authorizationHeader = headers.getFirst("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.warn("WebSocket Handshake: Authorization 헤더가 없습니다.");
            return false;
        }

        String token = authorizationHeader.substring(7);
        Long userId = jwtUtil.extractUserIdFromToken(token);

        if (userId == null) {
            log.warn("WebSocket Handshake: 유효하지 않은 토큰입니다.");
            return false;
        }

        log.info("WebSocket Handshake 성공: userId={}", userId);
        attributes.put("USER_ID", userId);
        return true;
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler webSocketHandler,
            Exception exception) {
    }
}