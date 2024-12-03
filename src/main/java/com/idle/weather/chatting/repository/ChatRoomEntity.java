package com.idle.weather.chatting.repository;

import com.idle.weather.global.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chat_room")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ChatRoomEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotNull
    private String name;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ChatRoomMemberEntity> members = new ArrayList<>();

    @Column(nullable = false)
    private boolean isActivated;

    @Column(name = "parent_region_code")
    @NotNull
    private String parentRegionCode;

    @Column(name = "parent_region_name")
    @NotNull
    private String parentRegionName;

    @Column(name = "deactivated_at")
    private LocalDateTime deactivatedAt;

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<WeatherAlertEntity> weatherAlerts = new ArrayList<>();

    public static ChatRoomEntity createChatRoom(String parentRegionCode, String parentRegionName) {
        return ChatRoomEntity.builder()
                .name(parentRegionName + " 채팅방")
                .isActivated(true)
                .parentRegionCode(parentRegionCode)
                .parentRegionName(parentRegionName)
                .build();
    }

    public void addWeatherAlert(WeatherAlertEntity weatherAlertEntity) {
        this.weatherAlerts.add(weatherAlertEntity);
        weatherAlertEntity.updateChatRoom(this);
        updateChatRoomName();
    }

    public void removeWeatherAlert(WeatherAlertEntity weatherAlertEntity) {
        this.weatherAlerts.remove(weatherAlertEntity);
        weatherAlertEntity.updateChatRoom(null);
        updateChatRoomName();

        if (this.weatherAlerts.stream().noneMatch(WeatherAlertEntity::isActivated)) {
            deactivateChatRoom();
        }
    }

    public void updateChatRoomName() {
        List<String> alertTypes = this.weatherAlerts.stream()
                .filter(WeatherAlertEntity::isActivated)
                .map(WeatherAlertEntity::getAlertType)
                .distinct()
                .toList();

        if (alertTypes.isEmpty()) {
            this.name = this.parentRegionName + " 채팅방";
        } else {
            String alertTypesString = String.join(", ", alertTypes);
            this.name = this.parentRegionName + " " + alertTypesString + " 경보 채팅방";
        }
    }

    public void deactivateChatRoom() {
        if (this.isActivated) {
            this.isActivated = false;
            this.deactivatedAt = LocalDateTime.now();
        }
    }
}
