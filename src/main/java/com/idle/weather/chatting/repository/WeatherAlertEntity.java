package com.idle.weather.chatting.repository;

import com.idle.weather.global.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "weather_alert")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class WeatherAlertEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parent_region_code")
    @NotNull
    private String parentRegionCode; // 상위 특보 구역 코드

    @Column(name = "parent_region_name")
    @NotNull
    private String parentRegionName; // 상위 특보 구역 이름

    @Column(name = "region_code")
    @NotNull
    private String regionCode; // 특보 구역 코드

    @Column(name = "region_name")
    private String regionName; // 특보 구역 이름

    @Column(name = "announcement_time")
    @NotNull
    private String announcementTime; // 발표 시각

    @Column(name = "effective_time")
    @NotNull
    private String effectiveTime; // 발효 시각

    @Column(name = "alert_type")
    @NotNull
    private String alertType; // 특보 종류

    @Column(name = "alert_level")
    private String alertLevel; // 특보 수준

    @Column(name = "command")
    private String command; // 특보 명령

    @Column(name = "endTime")
    private LocalDateTime endTime; // 해제 예고 시점

    @Column(name = "is_activated")
    private boolean isActivated; // 기상특보 해제 여부

    @Column(name = "deactivated_at")
    private LocalDateTime deactivatedAt; // 비활성화 시점

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoomEntity chatRoom;

    public static WeatherAlertEntity createWeatherAlert(
            String parentRegionCode,
            String parentRegionName,
            String regionCode,
            String regionName,
            String announcementTime,
            String effectiveTime,
            String alertType,
            String alertLevel,
            String command,
            LocalDateTime endTime
    ) {
        return WeatherAlertEntity.builder()
                .parentRegionCode(parentRegionCode)
                .parentRegionName(parentRegionName)
                .regionCode(regionCode)
                .regionName(regionName)
                .announcementTime(announcementTime)
                .effectiveTime(effectiveTime)
                .alertType(alertType)
                .alertLevel(alertLevel)
                .command(command)
                .endTime(endTime)
                .isActivated(true)
                .build();
    }

    public void updateWeatherAlert(WeatherAlertEntity apiAlert) {
        this.endTime = apiAlert.getEndTime();
        this.command = apiAlert.getCommand();
        this.alertLevel = apiAlert.getAlertLevel();
    }

    public void deactivateWeatherAlert() {
        if (this.isActivated) {
            this.isActivated = false;
            this.deactivatedAt = LocalDateTime.now();

            if (this.chatRoom != null) {
                this.chatRoom.removeWeatherAlert(this);
            }
        }
    }

    public void updateChatRoom(ChatRoomEntity chatRoom) {
        this.chatRoom = chatRoom;
    }
}
