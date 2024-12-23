package com.idle.weather.websocket.config;

import com.idle.weather.util.JwtUtil;
import com.idle.weather.websocket.auth.JwtChannelInterceptor;
import com.idle.weather.websocket.auth.JwtHandshakeInterceptor;
import com.idle.weather.websocket.interceptor.StompUserIdArgumentResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtUtil jwtUtil;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        log.info("Registering STOMP endpoint /api/ws/chat");
        registry.addEndpoint("/api/ws/chat") // WebSocket 엔트포인트
                .setAllowedOrigins("*") // 허용 도메인
                .addInterceptors(new JwtHandshakeInterceptor(jwtUtil));
                //.withSockJS(); // SockJs 지원
        log.info("Sunny : addInterceptors에서 JwtHandshakeInterceptor(jwtUtil) 호출");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // 토픽 브로커 활성화
        config.setApplicationDestinationPrefixes("/app"); // 클라이언트가 보낼 prefix
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new JwtChannelInterceptor(jwtUtil)); // STOMP 메시지 송수신 시 JWT 인증을 처리
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new StompUserIdArgumentResolver());
    }

}
