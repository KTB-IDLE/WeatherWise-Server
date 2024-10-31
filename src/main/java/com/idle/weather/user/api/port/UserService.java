package com.idle.weather.user.api.port;

import com.idle.weather.user.api.response.UserResponse;
import com.idle.weather.user.domain.User;
import com.idle.weather.user.dto.AuthSignUpDto;
import com.idle.weather.user.dto.SurveyDto;
import com.idle.weather.user.repository.UserEntity;

import java.util.Optional;

public interface UserService {
    UserResponse signUp(AuthSignUpDto authSignUpDto);
    UserResponse findById(Long id);
    UserResponse findBySerialId(String serialId);
    void updatePassword(Long userId, String newPassword);
    UserResponse updateNickname(Long userId, String newNickname);
    void deleteUser(Long userId);
    UserResponse reActivateUser(String serialId, String newPassword);
    void updateRefreshToken(Long userId, String refreshToken, boolean isLogin);
    boolean checkSurvey(Long userId);

    void applySurveyResult(Long userId, SurveyDto surveyResult);
}
