package com.idle.weather.mock;

import com.idle.weather.mission.domain.CurrentWeatherResponse;
import com.idle.weather.mission.domain.MissionType;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

/**
 * AI 서버 용 MOCK SERVICE
 */
@Service
public class MockFastApiService {
    public boolean missionAuthentication() {
        // 무조건 성공이라고 가정
        return true;
        // 무조건 실패라고 가정
        // return false;
    }

    public CurrentWeatherResponse getCurrentWeatherInfo(int nx , int ny) {
        // 파이썬 서버에 nx , ny 보내고 해당 좌표의 날씨 정보를 가지고 오는 코드
        // 해당 응답을 적절하게 파싱해서 CurrentWeatherResponse 객체로 바꿔야한다. (현재 필요한 값만을 가지고 있는)
        return new CurrentWeatherResponse(MissionType.HOT);
    }
}
