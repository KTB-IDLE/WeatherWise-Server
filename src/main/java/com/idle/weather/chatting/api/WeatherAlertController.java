package com.idle.weather.chatting.api;

import com.idle.weather.chatting.api.port.WeatherAlertService;
import com.idle.weather.chatting.api.response.WeatherAlertResponse;
import com.idle.weather.global.BaseResponse;
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

    @GetMapping
    public ResponseEntity<BaseResponse<List<WeatherAlertResponse>>> getAllWeatherAlert() {
        return ResponseEntity.ok(new BaseResponse<>(weatherAlertService.getAllWeatherAlerts()));
    }

    @GetMapping("/{weatherAlertId}")
    public ResponseEntity<BaseResponse<WeatherAlertResponse>> getWeatherAlertById(@PathVariable Long weatherAlertId) {
        return ResponseEntity.ok(new BaseResponse<>(weatherAlertService.getWeatherAlertById(weatherAlertId)));
    }

    @GetMapping("/activated")
    public ResponseEntity<BaseResponse<List<WeatherAlertResponse>>> getAllActivatedWeatherAlerts() {
        return ResponseEntity.ok(new BaseResponse<>(weatherAlertService.getAllActivatedWeatherAlerts()));
    }

    @PostMapping("/update")
    public ResponseEntity<Void> updateWeatherAlerts() {
        weatherAlertService.updateWeatherAlerts();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/deactivate-expired")
    public ResponseEntity<Void> deactivateExpiredAlerts() {
        weatherAlertService.deactivateExpiredAlerts();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete-old")
    public ResponseEntity<Void> deleteOldDeactivatedAlerts(@RequestParam(defaultValue = "7") int day) {
        weatherAlertService.deleteOldDeactivatedAlerts(day);
        return ResponseEntity.noContent().build();
    }
}
