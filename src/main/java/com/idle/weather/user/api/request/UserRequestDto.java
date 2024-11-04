package com.idle.weather.user.api.request;

import com.idle.weather.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public record UserRequestDto(UserInfo userInfo) {

    public record UserInfo(boolean easilyHot, boolean easilyCold, boolean easilySweat) {
        public static UserInfo from(User user) {
            return new UserInfo(
                    user.isEasilyHot(),
                    user.isEasilyCold(),
                    user.isEasilySweat()
            );
        }
    }
}
