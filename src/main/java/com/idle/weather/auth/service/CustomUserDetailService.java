package com.idle.weather.auth.service;

import com.idle.weather.auth.CustomUserDetails;
import com.idle.weather.exception.BaseException;
import com.idle.weather.exception.ErrorCode;
import com.idle.weather.user.service.port.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        log.info(username);
        UserRepository.UserSecurityForm user = userRepository.findUserIdAndRoleBySerialId(username)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER));

        return CustomUserDetails.create(user);
    }

    public UserDetails loadUserById(Long userId) throws BaseException {
        UserRepository.UserSecurityForm user = userRepository.findByIdAndIsLoginAndRefreshTokenIsNotNull(userId, true)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found UserId"));

        return CustomUserDetails.create(user);
    }
}