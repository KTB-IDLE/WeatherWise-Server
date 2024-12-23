package com.idle.weather.home;

import com.idle.weather.user.api.request.SurveyRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter @Builder
@AllArgsConstructor @NoArgsConstructor
public class SendUserInfoDto {
    private List<Integer> answers; // JSON 배열을 List로 매핑
    private Long userId;

    public static SendUserInfoDto of(SurveyRequestDto surveyResult, Long userId) {
        return SendUserInfoDto.builder()
                .userId(userId)
                .answers(surveyResult.getAnswers())
                .build();
    }
}
