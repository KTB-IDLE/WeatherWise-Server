package com.idle.weather.user.api.response;

import com.idle.weather.user.dto.type.EProvider;
import com.idle.weather.user.dto.type.ERole;
import com.idle.weather.user.repository.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserResponse(
        @Schema(description = "사용자 ID", example = "1")
        Long userId,

        @Schema(description = "사용자 회원가입 ID", example = "meowmeow@gmail.com")
        String serialId,

        @Schema(description = "로그인 경로", example = "KAKAO")
        EProvider provider,

        @Schema(description = "권한", example = "USER")
        ERole role,

        @Schema(description = "닉네임", example = "카페라떼냥")
        String nickname,

        @Schema(description = "생성 시간")
        LocalDateTime createdAt
) {
        public static UserResponse from(UserEntity userEntity) {
                return new UserResponse(
                        userEntity.getId(),
                        userEntity.getSerialId(),
                        userEntity.getProvider(),
                        userEntity.getRole(),
                        userEntity.getNickname(),
                        userEntity.getCreatedAt()
                );
        }
}
