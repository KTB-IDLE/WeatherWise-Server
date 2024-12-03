package com.idle.weather.chatting.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.idle.weather.chatting.repository.WeatherAlertEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExternalWeatherApiClient {
    //Todo : 항목 데이터(필드개수)가 몇개 빠져있을 경우 어떻게 처리할 것인지 확인. 현재는 조건문으로 누락된 필드가 2개 이상인 경보일 경우 무시함.

    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper;

    @Value("${weather.api.auth-key}")
    private String authKey;

    public List<WeatherAlertEntity> fetchWeatherAlerts() {
        try {
            String response = webClientBuilder.build()
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("https")
                            .host("apihub.kma.go.kr")
                            .path("/api/typ01/url/wrn_now_data.php")
                            .queryParam("fe", "f")
                            .queryParam("disp", 0)
                            .queryParam("authKey", authKey)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return parseWeatherAlerts(response);
        } catch (Exception e) {
            log.error("기상특보 API 호출 중 에러 발생", e);
            throw new RuntimeException("기상특보 API 호출 중 에러 발생", e);
        }
    }

    private List<WeatherAlertEntity> parseWeatherAlerts(String response) {
        try {
            String[] lines = response.split("\n");

            List<WeatherAlertEntity> weatherAlertEntityList = new ArrayList<>();

            for (String line : lines) {
                if (line.trim().isEmpty() || line.startsWith("#")) { // 비어있는 줄이거나 '#'으로 시작할 경우 무시
                    continue;
                }

                String[] fields = line.split(","); // ','로 데이터 구분
                if (fields.length < 7) { // 누락된 필드가 있는 경보일 경우 무시
                    log.warn("필드 개수가 부족한 데이터 무시: {}", line);
                    continue;
                }

                String alertLevel = fields[7].trim(); // '경보', '주의', '예비' 중 하나

                // '경보'가 아닌 경우 필터링
//                if (!"경보".equals(alertLevel)) {
//                    continue;
//                }

                WeatherAlertEntity weatherAlertEntity = WeatherAlertEntity.createWeatherAlert(
                        fields[0].trim(), // regUp
                        fields[1].trim(), // regUpKo
                        fields[2].trim(), // regId
                        fields[3].trim(), // regKo
                        fields[4].trim(), // tmFc
                        fields[5].trim(), // tmEf
                        fields[6].trim(), // wrn
                        alertLevel, //fields[7].trim(), // lvl
                        fields[8].trim(), // cmd
                        parseEndTime(fields[9].trim()) // edTm
                );
                weatherAlertEntityList.add(weatherAlertEntity);
            }
            return weatherAlertEntityList;
        } catch (Exception e) {
            throw new RuntimeException("기상특보 데이터를 파싱하는 중 에러 발생", e);
        }
    }

    private LocalDateTime parseEndTime(String edTm) {
        if (edTm == null || edTm.isEmpty() || edTm.equals(" ") || edTm.equals("00일")) {
            return null; // 해제 예고 시점이 없을 경우
        }

        try {
            Pattern pattern = Pattern.compile("(\\d+)일\\s*(.*)");
            Matcher matcher = pattern.matcher(edTm);
            if (matcher.matches()) {
                int dayOffset = Integer.parseInt(matcher.group(1).trim());
                String timeRange = matcher.group(2).trim();

                LocalDate today = LocalDate.now();
                LocalDate targetDate = today.plusDays(dayOffset - 1);

                LocalTime time = mapTimeRangeToLocalTime(timeRange);

                return LocalDateTime.of(targetDate, time);
            } else {
                log.warn("예상치 못한 edTm 형싱 : '{}'", edTm);
                return null;
            }
        } catch (Exception e) {
            log.warn("endTime 파싱 실패: '{}', 오류: {}", edTm, e.getMessage());
            return null;
        }
    }

    private LocalTime mapTimeRangeToLocalTime(String timeRange) {
        switch (timeRange) {
            case "새벽(00시~03시)":
                return LocalTime.of(3, 0);
            case "새벽(03시~06시)":
                return LocalTime.of(6, 0);
            case "아침(06시~09시)":
                return LocalTime.of(9, 0);
            case "오전(09시~12시)":
                return LocalTime.of(12, 0);
            case "낮(12시~15시)":
                return LocalTime.of(13, 30);
            case "오후(12시~18시)":
                return LocalTime.of(15, 0);
            case "늦은 오후(15시~18시)":
                return LocalTime.of(18, 0);
            case "저녁(18시~21시)":
                return LocalTime.of(21, 0);
            case "밤(21시~24시)":
                return LocalTime.of(0, 0);
            default:
                log.warn("예상치 못한 시간대: '{}'", timeRange);
                return LocalTime.of(12, 0); // 기본값 설정
        }
    }

    private int extractDayFromEdTm(String edTm) {
        try {
            // 예: "01일" 추출
            String dayString = edTm.split("일")[0].trim();
            return Integer.parseInt(dayString);
        } catch (Exception e) {
            throw new IllegalArgumentException("While Extracting Day from edTm, Error invalid date format in edTm: " + edTm, e);
        }
    }

    private LocalTime extractMaxTimeFromEdTm(String edTm) {
        if (edTm.contains("오전")) {
            return LocalTime.of(12, 0); // 오전: 12:00
        } else if (edTm.contains("오후")) {
            return LocalTime.of(18, 0); // 오후: 18:00
        } else if (edTm.contains("저녁") || edTm.contains("늦은 오후")) {
            return LocalTime.of(21, 0); // 저녁: 21:00
        } else if (edTm.contains("밤")) {
            return LocalTime.of(0, 0); // 밤: 24:00 (다음날 00:00)
        } else if (edTm.contains("새벽")) {
            return LocalTime.of(6, 0); // 새벽: 06:00
        } else {
            log.warn("예상치 못한 시간대: '{}'", edTm);
            return LocalTime.of(12, 0); // 기본값 설정
        }
    }
}
