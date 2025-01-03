package com.idle.weather.user.domain;

import com.idle.weather.missionhistory.domain.MissionHistory;
import com.idle.weather.missionhistory.domain.SuccessMissionHistory;
import com.idle.weather.user.api.request.SurveyRequestDto;
import com.idle.weather.user.dto.SurveyDto;
import com.idle.weather.user.dto.type.EProvider;
import com.idle.weather.user.dto.type.ERole;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.*;
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

    private SurveyResult surveyResult;
    private boolean isDeleted;
    private boolean isCompletedSurvey;

    public void updatedExperience(int point) {
        this.point = this.point + point;
    }
    public void levelUp(int remainValue) {
        this.level++;
        this.point = remainValue;
    }
    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }
    public void updateInfo(String nickname) {
        if (nickname != null && (!Objects.equals(this.nickname, nickname))) {
            this.nickname = nickname;
        }
    }
    public void applySurveyResult(SurveyRequestDto surveyResult) {
        this.surveyResult = SurveyResult.of(surveyResult);
        this.isCompletedSurvey = true;
    }
    public void delete() {
        this.isDeleted = true;
        this.isLogin = false;
        this.refreshToken = null;
    }

    public void gainPoint(int point) {
        int totalPoint = this.point + point;
        if (totalPoint >= 100) {
            this.level++;
            this.point = totalPoint - this.point;
        } else {
            this.point += point;
        }
    }
}

