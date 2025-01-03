package com.idle.weather.websocket.auth;

import com.idle.weather.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;

@Slf4j
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        String authorizationHeader = accessor.getFirstNativeHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.warn("STOMP 메시지 Authorization 헤더가 없거나 유효하지 않습니다.");
            throw new IllegalArgumentException("Authorization header가 없거나 유효하지 않습니다");
        }

        String token = authorizationHeader.substring(7);

        Long userId = jwtUtil.extractUserIdFromToken(token);

        if (userId == null) {
            throw new IllegalArgumentException("유효하지 않거나 만료된 토큰입니다");
        }

        String userIdStr = userId.toString();

        accessor.setHeader("USER_ID", userIdStr);

        return MessageBuilder.createMessage(message.getPayload(), accessor.getMessageHeaders());
    }
}
