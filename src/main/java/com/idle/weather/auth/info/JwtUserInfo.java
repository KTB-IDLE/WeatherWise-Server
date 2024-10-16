package com.idle.weather.auth.info;

import com.idle.weather.user.dto.type.ERole;

public record JwtUserInfo(Long id, ERole role) {

}
