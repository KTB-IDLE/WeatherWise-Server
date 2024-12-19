package com.idle.weather.websocket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.idle.weather.chatting.api.port.ChatMessageService;
import com.idle.weather.chatting.api.request.ChatMessageRequest;
import com.idle.weather.chatting.api.response.ChatMessageResponse;
import com.idle.weather.util.JwtUtil;
import com.idle.weather.websocket.dto.WebSocketChatMessageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ChatMessageService chatMessageService;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 채팅방 관리 : chatRoomId -> 세션 목록
    private final Map<Long, Set<WebSocketSession>> chatRooms = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Long userId = (Long) session.getAttributes().get("USER_ID");
        log.info("WebSocket 연결됨: userId={}, sessionId={}", userId, session.getId());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            // 클라이언트에서 보낸 메시지 파싱
            String payload = message.getPayload();
            WebSocketChatMessageRequest chatMessageRequest = objectMapper.readValue(payload, WebSocketChatMessageRequest.class);

            String authHeader = session.getHandshakeHeaders().getFirst("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new IllegalArgumentException("Authorization header가 없거나 유효하지 않습니다 authHeader : " + authHeader);
            }

            String token = authHeader.substring(7);

            Long userId = jwtUtil.extractUserIdFromToken(token);
            if (userId == null) {
                throw new IllegalArgumentException("유효하지 않거나 만료된 토큰입니다.");
            }

            Long chatRoomId = chatMessageRequest.chatRoomId();

            chatRooms.computeIfAbsent(chatRoomId, k -> ConcurrentHashMap.newKeySet()).add(session);

            ChatMessageResponse chatMessageResponse = chatMessageService.sendMessage(chatRoomId, ChatMessageRequest.from(chatMessageRequest.message()), userId);

            broadcastMessage(chatMessageResponse);

        } catch (Exception e) {
            log.error("메시지 처리 중 오류 발생: ", e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        chatRooms.forEach((chatRoomId, sessions) -> sessions.remove(session)); //TODO : 뭐지
        log.info("WebSocket 연결 종료: sessionId={}", session.getId());
    }

    private void broadcastMessage(ChatMessageResponse response) {
        // 해당 채팅방에 있는 모든 세션에 메시지 전송
        Set<WebSocketSession> sessions = chatRooms.getOrDefault(response.chatRoomId(), Collections.emptySet());
        sessions.forEach(session -> {
            try {
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
            } catch (Exception e) {
                log.error("메시지 브로드캐스트 중 오류 발생: ", e);
            }
        });
    }


}
