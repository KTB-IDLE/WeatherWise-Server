package com.idle.weather.chatting.api.response;

import com.idle.weather.chatting.repository.WeatherAlertEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record WeatherAlertResponse(
        @Schema(description = "기상특보 ID", example = "1")
        Long weatherAlertId,

        @Schema(description = "상위 특보 구역 코드", example = "L1100000")
        String parentRegionCode,

        @Schema(description = "상위 특보 구역 이름", example = "서울특별시")
        String parentRegionName,

        @Schema(description = "특보 구역 코드", example = "L1100100")
        String regionCode,

        @Schema(description = "특보 구역 이름", example = "서울 동남권")
        String regionName,

        @Schema(description = "발표 시각", example = "202411261600")
        String announcementTime,

        @Schema(description = "발효 시각", example = "202411261800")
        String effectiveTime,

        @Schema(description = "특보 종류", example = "지진")
        String alertType,

        @Schema(description = "특보 수준", example = "경보")
        String alertLevel,

        @Schema(description = "특보 명령", example = "발표")
        String command,

        @Schema(description = "해제 예고 시점", example = "202411271200")
        LocalDateTime endTime,

        @Schema(description = "기상특보 활성화 여부", example = "1")
        boolean isActivated,

        @Schema(description = "연관된 채팅방 ID", example = "1")
        Long chatRoomId,

        @Schema(description = "연관된 채팅방 이름", example = "동해남부전해상 풍랑 경보 채팅방")
        String chatRoomName
) {
        public static WeatherAlertResponse from(WeatherAlertEntity weatherAlertEntity) {
                return new WeatherAlertResponse(
                        weatherAlertEntity.getId(),
                        weatherAlertEntity.getParentRegionCode(),
                        weatherAlertEntity.getParentRegionName(),
                        weatherAlertEntity.getRegionCode(),
                        weatherAlertEntity.getRegionName(),
                        weatherAlertEntity.getAnnouncementTime(),
                        weatherAlertEntity.getEffectiveTime(),
                        weatherAlertEntity.getAlertType(),
                        weatherAlertEntity.getAlertLevel(),
                        weatherAlertEntity.getCommand(),
                        weatherAlertEntity.getEndTime(),
                        weatherAlertEntity.isActivated(),
                        weatherAlertEntity.getChatRoom().getId(),
                        weatherAlertEntity.getChatRoom().getName()
                );
        }
}
