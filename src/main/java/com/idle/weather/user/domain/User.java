package com.idle.weather.user.domain;

import com.idle.weather.missionhistory.domain.MissionHistory;
import com.idle.weather.user.dto.type.EProvider;
import com.idle.weather.user.dto.type.ERole;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private boolean runHot;
    private boolean runCold;
    private boolean runSweat;

    public void updatedExperience(int point) {
        this.point = this.point + point;
    }
    public void levelUp(int remainValue) {
        this.level++;
        this.point = remainValue;
    }
}

