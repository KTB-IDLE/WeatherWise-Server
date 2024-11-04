package com.idle.weather.user.api.request;

import com.idle.weather.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserRequestDto {

    @NoArgsConstructor @AllArgsConstructor
    @Builder @Getter
    public static class UserInfo {
        private boolean easilyHot;
        private boolean easilyCold;
        private boolean easilySweat;

        public static UserInfo from(User user) {
            return UserInfo.builder()
                    .easilyCold(user.isEasilyCold())
                    .easilyHot(user.isEasilyHot())
                    .easilySweat(user.isEasilySweat())
                    .build();

        }
    }
}
