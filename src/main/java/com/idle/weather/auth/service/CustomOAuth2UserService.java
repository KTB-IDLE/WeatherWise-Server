package com.idle.weather.auth.service;

import com.idle.weather.auth.CustomUserDetails;
import com.idle.weather.auth.info.factory.OAuth2UserInfo;
import com.idle.weather.auth.info.factory.OAuth2UserInfoFactory;
import com.idle.weather.user.dto.type.EProvider;
import com.idle.weather.user.dto.type.ERole;
import com.idle.weather.user.repository.UserEntity;
import com.idle.weather.user.repository.UserJpaRepository;
import com.idle.weather.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserJpaRepository userJpaRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        try {
            return this.process(userRequest, super.loadUser(userRequest));
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public OAuth2User process(OAuth2UserRequest userRequest, OAuth2User oauth2User) {
        EProvider provider = EProvider.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(provider, oauth2User.getAttributes());

        UserJpaRepository.UserSecurityForm userOpt = userJpaRepository.findBySerialIdAndProvider(userInfo.getId(), provider)
                .orElseGet(() -> {
                    UserEntity user = userJpaRepository.save(new UserEntity(
                            userInfo.getId(),
                            bCryptPasswordEncoder.encode(PasswordUtil.generateRandomPassword()),
                            provider,
                            ERole.USER,
                            userInfo.getNickName()
                    ));
                    return UserJpaRepository.UserSecurityForm.invoke(user);
                });

        return CustomUserDetails.create(userOpt, userInfo.getAttributes());
    }
}
