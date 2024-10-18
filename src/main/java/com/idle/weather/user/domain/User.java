package com.idle.weather.user.domain;

import com.idle.weather.missionhistory.domain.MissionHistory;
import com.idle.weather.missionhistory.repository.MissionHistoryEntity;
import com.idle.weather.user.dto.AuthSignUpDto;
import com.idle.weather.user.dto.type.EProvider;
import com.idle.weather.user.dto.type.ERole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Getter
@Builder
public class User {

    private Long id;
    private String serialId;
    private String password;
    private EProvider provider;
    private ERole role;
    private LocalDate createdDate;
    private String nickname;

    /* User Status */
    private Boolean isLogin;

    private String refreshToken;

    private int level;

    private int point;

    private List<MissionHistory> missionHistories = new ArrayList<>();

    public void updatedExperience(int point) {
        this.point = this.point + point;
    }
    public void levelUp(int remainValue) {
        this.level++;
        this.point = remainValue;
    }
}

