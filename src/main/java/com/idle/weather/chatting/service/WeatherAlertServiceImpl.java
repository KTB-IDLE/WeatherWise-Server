package com.idle.weather.chatting.service;

import com.idle.weather.chatting.api.port.WeatherAlertService;
import com.idle.weather.chatting.api.response.WeatherAlertResponse;
import com.idle.weather.chatting.client.ExternalWeatherApiClient;
import com.idle.weather.chatting.repository.ChatRoomEntity;
import com.idle.weather.chatting.repository.WeatherAlertEntity;
import com.idle.weather.chatting.repository.WeatherAlertJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherAlertServiceImpl implements WeatherAlertService {

    private final ExternalWeatherApiClient externalWeatherApiClient;
    private final WeatherAlertJpaRepository weatherAlertJpaRepository;
    private final ChatRoomServiceImpl chatRoomService;

    @Override
    @Transactional
    public void updateWeatherAlerts() {
        try {
            log.info("기상특보 업데이트 시작 ... ");
            List<WeatherAlertEntity> apiAlerts = externalWeatherApiClient.fetchWeatherAlerts();
            List<WeatherAlertEntity> dbAlerts = weatherAlertJpaRepository.findAllActivatedAlerts();

            Map<String, WeatherAlertEntity> dbAlertMap = dbAlerts.stream()
                    .collect(Collectors.toMap(this::generateUniqueKey, alert -> alert));

            for(WeatherAlertEntity apiAlert : apiAlerts) {
                String apiAlertKey = generateUniqueKey(apiAlert);
                WeatherAlertEntity dbAlert = dbAlertMap.get(apiAlertKey);

                if (dbAlert == null) {
                    weatherAlertJpaRepository.save(apiAlert);
                    log.info("새로운 기상특보 추가됨: {}", apiAlert);

                    ChatRoomEntity chatRoom = chatRoomService.getOrCreateChatRoom(apiAlert.getParentRegionCode(), apiAlert.getParentRegionName());

                    chatRoom.addWeatherAlert(apiAlert);
                    chatRoomService.saveChatRoom(chatRoom);
                } else {
                    if (hasAlertChanged(dbAlert, apiAlert)) {
                        dbAlert.updateWeatherAlert(apiAlert);
                        weatherAlertJpaRepository.save(dbAlert);
                        log.info("기상특보 업데이트됨: {}", dbAlert);

                        ChatRoomEntity chatRoom = dbAlert.getChatRoom();
                        if (chatRoom != null) {
                            chatRoom.updateChatRoomName();
                            chatRoomService.saveChatRoom(chatRoom);
                        }
                    }
                    dbAlertMap.remove(apiAlertKey); // 처리된 기상특보는 Map에서 제거
                }
            }

            // 남은 데이터베이스 기상특보는 API에 없으므로 비활성화
            for (WeatherAlertEntity dbAlert : dbAlertMap.values()) {
                dbAlert.deactivateWeatherAlert();
                weatherAlertJpaRepository.save(dbAlert);
                log.info("기상특보 비활성화 : {}", dbAlert);
            }

        } catch (Exception e) {
            log.error("기상특보 업데이트 실패", e);
            throw new RuntimeException("기상특보 업데이트 실패", e);
        }

    }

    @Override
    @Transactional
    public void deactivateExpiredAlerts() {
        log.info("해제 예고시점이 지난 기상특보 채팅방 비활성화 시작 ... ");
        List<WeatherAlertEntity> expiredAlerts = weatherAlertJpaRepository.findExpiredAlerts();

        for (WeatherAlertEntity expiredAlert : expiredAlerts) {
            expiredAlert.deactivateWeatherAlert();
            weatherAlertJpaRepository.save(expiredAlert);
            log.info("해제 예고시점이 지난 기상특보 비활성화 : {}", expiredAlert);
        }
    }

    @Override
    @Transactional
    public void deleteOldDeactivatedAlerts(int day) {
        log.info("오래된 비활성화 기상특보 삭제 시작 ...");
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(day);
        List<WeatherAlertEntity> oldDeactivatedAlerts = weatherAlertJpaRepository.findAllDeactivatedOlderThan(cutoffDate);

        for (WeatherAlertEntity oldDeactivatedAlert : oldDeactivatedAlerts) {
            weatherAlertJpaRepository.delete(oldDeactivatedAlert);
            log.info("오래된 비활성화 기상특보 삭제 : {}", oldDeactivatedAlert);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<WeatherAlertResponse> getAllWeatherAlerts() {
        return weatherAlertJpaRepository.findAll().stream()
                .map(WeatherAlertResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<WeatherAlertResponse> getAllActivatedWeatherAlerts() {
        return weatherAlertJpaRepository.findAllActivatedAlerts().stream()
                .map(WeatherAlertResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public WeatherAlertResponse getWeatherAlertById(Long weatherAlertId) {
        WeatherAlertEntity weatherAlertEntity = weatherAlertJpaRepository.findById(weatherAlertId)
                .orElseThrow(() -> new IllegalArgumentException("WeatherAlert not found with weatherAlert ID : " + weatherAlertId));
        return WeatherAlertResponse.from(weatherAlertEntity);
    }

    @Override
    public List<WeatherAlertEntity> getRegionList(String region) {
        return weatherAlertJpaRepository.findByKeywordInChatRoomOrParentRegion(region);
    }

    private boolean hasAlertChanged(WeatherAlertEntity dbAlert, WeatherAlertEntity apiAlert) {
        return !Objects.equals(dbAlert.getEndTime(), apiAlert.getEndTime()) ||
                !Objects.equals(dbAlert.getCommand(), apiAlert.getCommand()) ||
                !Objects.equals(dbAlert.getAlertLevel(), apiAlert.getAlertLevel());
    }

    private String generateUniqueKey(WeatherAlertEntity weatherAlertEntity) {
        String uniqueHashKey = DigestUtils.sha256Hex(
                weatherAlertEntity.getParentRegionCode() + "|" +
                        weatherAlertEntity.getRegionCode() + "|" +
                        weatherAlertEntity.getAnnouncementTime() + "|" +
                        weatherAlertEntity.getEffectiveTime() + "|" +
                        weatherAlertEntity.getAlertType()
        );
        return uniqueHashKey;
    }

}
