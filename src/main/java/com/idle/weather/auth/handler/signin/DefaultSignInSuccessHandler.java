package com.idle.weather.auth.handler.signin;

import com.idle.weather.auth.CustomUserDetails;
import com.idle.weather.auth.dto.JwtTokenDto;
import com.idle.weather.user.dto.type.ERole;
import com.idle.weather.user.service.port.UserRepository;
import com.idle.weather.util.CookieUtil;
import com.idle.weather.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONValue;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class DefaultSignInSuccessHandler implements AuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Value("${after-login.default-success}")
    private String LOGIN_URL;
    
    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomUserDetails userPrincipal = (CustomUserDetails) authentication.getPrincipal();
        ERole role = userPrincipal.getRole();

        // JWT Token 발급

        JwtTokenDto jwtTokenDto = jwtUtil.generateTokens(userPrincipal.getId(), userPrincipal.getRole());
        userRepository.updateRefreshTokenAndLoginStatus(userPrincipal.getId(), jwtTokenDto.getRefreshToken(), true);
        String nickname = userRepository.findById(userPrincipal.getId())
                .map(user -> StringUtils.hasText(user.getNickname()) ? user.getNickname() : user.getSerialId())
                .orElseThrow();

        String userAgent = request.getHeader("User-Agent");

        boolean isMobile = userAgent.matches(".*(iPhone|iPod|iPad|BlackBerry|Android|Windows CE|LG|MOT|SAMSUNG|SonyEricsson).*");

        if (userAgent.indexOf("IOS_APP") > -1 || userAgent.indexOf("ANDROID_APP") > -1) {
            log.info("앱");
            setSuccessAppResponse(response, jwtTokenDto);
        } else if (isMobile) {
            log.info("앱브라우저");
            setSuccessWebResponse(response, jwtTokenDto, nickname, role);
        } else {
            log.info("웹");
            setSuccessWebResponse(response, jwtTokenDto, nickname, role);
        }
    }

    private void setSuccessAppResponse(HttpServletResponse response, JwtTokenDto tokenDto) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.OK.value());

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", Map.of(
                "accessToken", tokenDto.getAccessToken(),
                "refreshToken", tokenDto.getRefreshToken()
        ));
        result.put("error", null);

        response.getWriter().write(JSONValue.toJSONString(response));
    }

    private void setSuccessWebResponse(HttpServletResponse response, JwtTokenDto tokenDto, String nickname, ERole eRole) throws IOException {
        CookieUtil.addSecureCookie(response, "refreshToken", tokenDto.getRefreshToken(), jwtUtil.getWebRefreshTokenExpirationSecond());
        CookieUtil.addCookie(response, "accessToken", tokenDto.getAccessToken());
        //CookieUtil.addCookie(response, "nickname", URLEncoder.encode(nickname, StandardCharsets.UTF_8));
        //CookieUtil.addCookie(response, "role", eRole.getDisplayName());

        response.getWriter().println("OK");
    }
}
