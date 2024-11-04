package com.idle.weather.auth.handler.signin;

import com.idle.weather.auth.CustomUserDetails;
import com.idle.weather.auth.dto.JwtTokenDto;
import com.idle.weather.user.dto.type.ERole;
import com.idle.weather.user.repository.UserEntity;
import com.idle.weather.user.service.port.UserRepository;
import com.idle.weather.util.CookieUtil;
import com.idle.weather.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Value("${after-login.oauth2-success}")
    private String LOGIN_URL;

    @Value("${after-login.oauth2-success-guest}")
    private String LOGIN_URL_GUEST;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        CustomUserDetails userPrincipal = (CustomUserDetails) authentication.getPrincipal();

        JwtTokenDto jwtTokenDto = jwtUtil.generateTokens(userPrincipal.getId(), userPrincipal.getRole());
        userRepository.updateRefreshTokenAndLoginStatus(userPrincipal.getId(), jwtTokenDto.getRefreshToken(), true);
        String nickname = userRepository.findByIdForLegacy(userPrincipal.getId()).map(UserEntity::getNickname).orElse("");

        CookieUtil.addSecureCookie(response, "refreshToken", jwtTokenDto.getRefreshToken(), jwtUtil.getWebRefreshTokenExpirationSecond());
        CookieUtil.addCookie(response, "accessToken", jwtTokenDto.getAccessToken());
        CookieUtil.addCookie(response, "nickname", URLEncoder.encode(nickname, StandardCharsets.UTF_8));
        CookieUtil.addCookie(response, "role", userPrincipal.getRole().getDisplayName());

        if (userPrincipal.getRole() == ERole.GUEST) {
            response.sendRedirect(LOGIN_URL_GUEST);
        } else {
            response.sendRedirect(LOGIN_URL);
        }
    }
}
