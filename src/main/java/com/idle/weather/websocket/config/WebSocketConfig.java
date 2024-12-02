package com.idle.weather.websocket.config;

import com.idle.weather.util.JwtUtil;
import com.idle.weather.websocket.auth.JwtChannelInterceptor;
import com.idle.weather.websocket.auth.JwtHandshakeInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtUtil jwtUtil;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        log.info("!!Registering STOMP endpoint /ws/chat!!");
        registry.addEndpoint("/ws/chat") // WebSocket 엔트포인트
                .setAllowedOrigins("*") // 허용 도메인
                .addInterceptors(new JwtHandshakeInterceptor(jwtUtil));
                //.withSockJS(); // SockJs 지원
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "queue"); // 메시지 브로커
        config.setApplicationDestinationPrefixes("/app"); // 클라이언트가 보낼 prefix
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new JwtChannelInterceptor(jwtUtil)); // STOMP 메시지 송수신 시 JWT 인증을 처리
    }

}