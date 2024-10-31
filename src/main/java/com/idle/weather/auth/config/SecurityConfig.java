package com.idle.weather.auth.config;

import com.idle.weather.auth.BasicAuthenticationProvider;
import com.idle.weather.auth.CustomAuthenticationProvider;
import com.idle.weather.auth.JwtAuthEntryPoint;
import com.idle.weather.auth.filter.JwtExceptionFilter;
import com.idle.weather.auth.filter.JwtFilter;
import com.idle.weather.auth.handler.JwtAccessDeniedHandler;
import com.idle.weather.auth.handler.signin.DefaultSignInFailureHandler;
import com.idle.weather.auth.handler.signin.DefaultSignInSuccessHandler;
import com.idle.weather.auth.handler.signin.OAuth2LoginFailureHandler;
import com.idle.weather.auth.handler.signin.OAuth2LoginSuccessHandler;
import com.idle.weather.auth.handler.signout.CustomSignOutProcessHandler;
import com.idle.weather.auth.handler.signout.CustomSignOutResultHandler;
import com.idle.weather.auth.service.CustomOAuth2UserService;
import com.idle.weather.auth.service.CustomUserDetailService;
import com.idle.weather.common.constant.Constants;
import com.idle.weather.user.dto.type.ERole;
import com.idle.weather.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final DefaultSignInSuccessHandler defaultSignInSuccessHandler;
    private final DefaultSignInFailureHandler defaultSignInFailureHandler;
    private final CustomUserDetailService customUserDetailService;
    private final BasicAuthenticationProvider basicAuthenticationProvider;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtUtil jwtUtil;
    private final CustomSignOutProcessHandler customSignOutProcessHandler;
    private final CustomSignOutResultHandler customSignOutResultHandler;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomAuthenticationProvider customAuthenticationProvider;


    @Bean
    protected SecurityFilterChain securityFilterChain(final HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // .cors(httpSecurityCorsConfigurer ->
                //         httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable) //보호 비활성화
                .httpBasic(AbstractHttpConfigurer::disable) // 기본 HTTP 기본 인증 비활성화
                .sessionManagement((sessionManagement) -> //상태를 유지하지 않는 세션 정책 설정
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(requestMatcherRegistry -> requestMatcherRegistry
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers(Constants.NO_NEED_AUTH_URLS).permitAll()
                        .requestMatchers("/api/v1/admins/**").hasRole(ERole.ADMIN.name())
                        .anyRequest().authenticated())
                //폼 기반 로그인 설정
                .formLogin(configurer ->
                        configurer
                                .loginPage("/login")
                                .loginProcessingUrl("/api/v1/sign-in") //로그인 처리 URL (POST)
                                .usernameParameter("serialId") //사용자 아이디 파라미터 이름
                                .passwordParameter("password") //비밀번호 파라미터 이름
                                .successHandler(defaultSignInSuccessHandler) //로그인 성공 핸들러
                                .failureHandler(defaultSignInFailureHandler) // 로그인 실패 핸들러
                ).userDetailsService(customUserDetailService) //사용자 검색할 서비스 설정
                //소셜 로그인
                .oauth2Login(configurer ->
                        configurer
                                .successHandler(oAuth2LoginSuccessHandler)
                                .failureHandler(oAuth2LoginFailureHandler)
                                .userInfoEndpoint(userInfoEndpoint ->
                                        userInfoEndpoint.userService(customOAuth2UserService)
                                )
                )
                // 로그아웃 설정
                .logout(configurer ->
                        configurer
                                .logoutUrl("/api/v1/logout")
                                .addLogoutHandler(customSignOutProcessHandler)
                                .logoutSuccessHandler(customSignOutResultHandler)
                                .deleteCookies("JSESSIONID", "nickname", "accessToken", "refreshToken", "role") // 쿠키 삭제 설정
                )

                //예외 처리 설정
                .exceptionHandling(configurer ->
                        configurer
                                .authenticationEntryPoint(jwtAuthEntryPoint)
                                .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                .addFilterBefore(new JwtFilter(jwtUtil, customAuthenticationProvider), LogoutFilter.class)
                .addFilterBefore(new JwtExceptionFilter(), JwtFilter.class)

                //SecurityFilterChain 빈을 반환
                .getOrBuild();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> {
            web.ignoring().requestMatchers("/swagger-ui/**", "/v3/api-docs/**");
        };
    }
}
