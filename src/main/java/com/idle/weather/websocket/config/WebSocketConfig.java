package com.idle.weather.websocket.config;

import com.idle.weather.chatting.api.port.ChatMessageService;
import com.idle.weather.util.JwtUtil;
import com.idle.weather.websocket.handler.ChatWebSocketHandler;
import com.idle.weather.websocket.auth.JwtHandshakeInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Slf4j
@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final JwtUtil jwtUtil;
    private final ChatWebSocketHandler chatWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatWebSocketHandler, "ws/chat")
                .setAllowedOrigins("*")
                .addInterceptors(new JwtHandshakeInterceptor(jwtUtil));
    }
}
