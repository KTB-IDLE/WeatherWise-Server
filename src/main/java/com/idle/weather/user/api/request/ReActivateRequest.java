package com.idle.weather.user.api.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record ReActivateRequest(
        @Schema(description = "사용자 회원가입 ID", example = "meow@gmail.com")
        String serialId,

        @Schema(description = "새로운 비밀번호", example = "newPassword!123!")
        String newPassword
) {
}
