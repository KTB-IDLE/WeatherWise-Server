package com.idle.weather.chatting.api;

import com.idle.weather.chatting.api.port.WeatherAlertService;
import com.idle.weather.chatting.api.response.WeatherAlertResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/weather-alerts")
public class WeatherAlertController {

    private final WeatherAlertService weatherAlertService;

    // 모든 기상특보 조회
    @GetMapping
    public ResponseEntity<List<WeatherAlertResponse>> getAllWeatherAlert() {
        List<WeatherAlertResponse> alerts = weatherAlertService.getAllWeatherAlerts();
        return ResponseEntity.ok(alerts);
    }

    // 특정 기상특보 조회
    @GetMapping("/{weatherAlertId}")
    public ResponseEntity<WeatherAlertResponse> getWeatherAlertById(@PathVariable Long weatherAlertId) {
        WeatherAlertResponse alert = weatherAlertService.getWeatherAlertById(weatherAlertId);
        return ResponseEntity.ok(alert);
    }

    // 활성화된 기상특보만 조회
    @GetMapping("/activated")
    public ResponseEntity<List<WeatherAlertResponse>> getAllActivatedWeatherAlerts() {
        List<WeatherAlertResponse> alerts = weatherAlertService.getAllActivatedWeatherAlerts();
        return ResponseEntity.ok(alerts);
    }

    // 기상특보 업데이트 수동 트리거 (테스트용)
    @PostMapping("/update")
    public ResponseEntity<Void> updateWeatherAlerts() {
        weatherAlertService.updateWeatherAlerts();
        return ResponseEntity.ok().build();
    }

    // 만료된 기상특보 비활성화 수동 트리거 (테스트용)
    @PostMapping("/deactivate-expired")
    public ResponseEntity<Void> deactivateExpiredAlerts() {
        weatherAlertService.deactivateExpiredAlerts();
        return ResponseEntity.ok().build();
    }

    // 오래된 비활성화된 기상특보 삭제 수동 트리거 (테스트용)
    @DeleteMapping("/delete-old")
    public ResponseEntity<Void> deleteOldDeactivatedAlerts(@RequestParam(defaultValue = "7") int day) {
        weatherAlertService.deleteOldDeactivatedAlerts(day);
        return ResponseEntity.noContent().build();
    }

}
