package com.idle.weather.missionhistory.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idle.weather.home.SendUserInfoDto;
import com.idle.weather.home.WeatherRequest;
import com.idle.weather.home.WeatherResponse;
import com.idle.weather.mission.api.request.MissionRequestDto;
import com.idle.weather.mission.domain.CurrentWeatherResponse;
import com.idle.weather.missionhistory.service.port.AIServerClient;
import com.idle.weather.user.api.request.SurveyRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static com.idle.weather.mission.api.request.MissionRequestDto.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class AIServerImpl implements AIServerClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${ai.endpoints.mission-auth}")
    private String aiMissionAuthEndpoints;

    @Value("${ai.endpoints.weather}")
    private String aiWeatherEndpoints;

    @Value("${ai.endpoints.user-info}")
    private String aiSendUserInfoEndpoints;

    private static final String AI_LOCAL_ENDPOINT = "http://localhost:8000/";


    @Override
    public boolean missionAuthentication(MissionAuth missionAuthDto) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MissionAuth> request = new HttpEntity<>(missionAuthDto, headers);
        String response = restTemplate.postForObject(aiMissionAuthEndpoints + "verification", request, String.class);
        JsonNode jsonNode = objectMapper.readTree(response);
        return jsonNode.get("certified").asBoolean();
    }

    @Override
    public WeatherResponse getCurrentWeatherInfo(double latitude, double longitude, Long userId) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        WeatherRequest weatherRequest = WeatherRequest.of(latitude, longitude, userId);
        HttpEntity<WeatherRequest> request = new HttpEntity<>(weatherRequest, headers);
        String response = restTemplate.postForObject(aiWeatherEndpoints + "weather_data", request, String.class);
        return objectMapper.readValue(response, WeatherResponse.class);
    }

    @Override
    public void sendUserInfo(SurveyRequestDto surveyResult, Long userId) {
        SendUserInfoDto dto = SendUserInfoDto.of(surveyResult, userId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SendUserInfoDto> request = new HttpEntity<>(dto, headers);
        restTemplate.postForObject(aiSendUserInfoEndpoints + "chat_with_pref", request, String.class);
    }
}
