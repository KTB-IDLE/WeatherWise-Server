package com.idle.weather.chatting.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor @Slf4j
@Transactional(readOnly = true)
public class LocationParsingService {

    private final RestTemplate restTemplate;

    @Value("${google.api.key}")
    private String googleApiKey;

    public String parsing(double latitude, double longitude) {
        log.info("api key = {} " , googleApiKey);
        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng="
                + latitude + "," + longitude + "&language=ko&key=" + googleApiKey;

        // API 호출
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        log.info("response = {} " , response);
        // 결과 파싱
        if (response.getStatusCode() == HttpStatus.OK) {
            Map body = response.getBody();
            String region = getRegion(body);
            return normalizeRegionKeyword(region);
        }
        return "존재하지 않음";
    }

    private String getRegion(Map body) {
        List<Map> results = (List<Map>) body.get("results");
        if (results != null && !results.isEmpty()) {
            for (Map result : results) {
                List<Map> addressComponents = (List<Map>) result.get("address_components");
                if (addressComponents == null) continue;

                for (Map component : addressComponents) {
                    List<String> types = (List<String>) component.get("types");
                    if (types.contains("administrative_area_level_1")) {
                        // 시·도 정보 예: 서울특별시, 제주특별자치도 등
                        String region = (String) component.get("long_name");
                        System.out.println("행정구역(시·도): " + region);
                        return region;
                    }
                }
            }
        }
        return "존재하지 않음";
    }

    public String normalizeRegionKeyword(String rawKeyword) {
        if (rawKeyword == null) {
            return "";
        }
        String keyword = rawKeyword;

        // 예: "강원특별자치도" -> "강원도"
        keyword = keyword.replace("특별자치", "");
        keyword = keyword.replace("광역", "");
        keyword = keyword.replace("특별", "");
        keyword = keyword.replace("시", ""); // 필요에 따라
        // 상황에 맞게 더 세분화할 수도 있음

        // 여기서는 "강원도"가 남도록 설계
        // 만약 "강원특별자치도" 최종적으로 "강원도"로 만들고 싶으면:
        // -> "강원특별자치도" -> "강원도"
        // 단, 실제 문자열 처리 로직은 케이스 바이 케이스로 직접 조정

        // 공백 제거 등 추가 정리
        keyword = keyword.trim();

        return keyword;
    }


}
