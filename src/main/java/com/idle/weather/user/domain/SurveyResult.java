package com.idle.weather.user.domain;

import com.idle.weather.user.api.request.SurveyRequestDto;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter @Builder
@NoArgsConstructor @AllArgsConstructor
public class SurveyResult {
    private int q1_answer;
    private int q2_answer;
    private int q3_answer;
    private int q4_answer;
    private int q5_answer;
    private int q6_answer;


    public static SurveyResult of(SurveyRequestDto surveyResult) {
        return SurveyResult.builder()
                .q1_answer(surveyResult.answers().get(0)+1)
                .q2_answer(surveyResult.answers().get(1)+1)
                .q3_answer(surveyResult.answers().get(2)+1)
                .q4_answer(surveyResult.answers().get(3)+1)
                .q5_answer(surveyResult.answers().get(4)+1)
                .q6_answer(surveyResult.answers().get(5)+1)
                .build();
    }
}
