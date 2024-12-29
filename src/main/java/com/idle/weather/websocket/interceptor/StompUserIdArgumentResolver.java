package com.idle.weather.websocket.interceptor;

import com.idle.weather.common.annotation.UserId;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

@Slf4j
public class StompUserIdArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Long.class)
                && parameter.hasParameterAnnotation(UserId.class);
    }


    @NotNull
    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  @Nullable Message<?> message) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String userIdStr = (String) accessor.getHeader("USER_ID");
        if (userIdStr == null) {
            throw new IllegalArgumentException("STOMP MESSAGE : USER_ID header를 찾지 못했습니다.");
        }
        return Long.valueOf(userIdStr);
    }
}
