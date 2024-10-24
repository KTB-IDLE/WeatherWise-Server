package com.idle.weather.mock;

import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

/**
 * AI 서버 용 MOCK SERVICE
 */
@Service
public class MockAuthFastApiService {
    public boolean send(HttpEntity<MultiValueMap<String, Object>> requestEntity) {
        // 무조건 성공이라고 가정
        // return true;
        // 무조건 실패라고 가정
        return false;
    }
}
