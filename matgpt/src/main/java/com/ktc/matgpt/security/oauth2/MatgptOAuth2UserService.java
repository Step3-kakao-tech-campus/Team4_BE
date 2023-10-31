package com.ktc.matgpt.security.oauth2;


import com.ktc.matgpt.exception.CustomException;
import com.ktc.matgpt.exception.ErrorCode;
import com.ktc.matgpt.security.UserPrincipal;
import com.ktc.matgpt.security.oauth2.userInfo.OAuth2UserInfo;
import com.ktc.matgpt.security.oauth2.userInfo.OAuth2UserInfoFactory;
import com.ktc.matgpt.user.entity.User;
import com.ktc.matgpt.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;


import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MatgptOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    // 유저 불러오기
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        //[리팩터링 요청] 예외 처리 구체화
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException("내부 인증오류");
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> userAttributes = oAuth2User.getAttributes();
        OAuth2UserInfo oauth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, userAttributes);

        if (isInvalidUserInfo(oauth2UserInfo)) {
            throw new CustomException(ErrorCode.OAUTH2_PROCESSING_EXCEPTION);
        }

        Optional<User> userOptional = userRepository.findByEmail(oauth2UserInfo.getEmail());
        User user;
        if(userOptional.isPresent()) {
            user = userOptional.get();
            if (requiresUpdateUser(user, oauth2UserInfo, registrationId)) {
                user = updateUser(user, oauth2UserInfo);
            }
        } else {
            //유저가 존재하지 않는 경우 회원가입 진행
            user = registerUser(oAuth2UserRequest, oauth2UserInfo);
        }
        return UserPrincipal.create(user, oauth2UserInfo);
    }

    private boolean isInvalidUserInfo(OAuth2UserInfo oauth2UserInfo) {
        // provider에 가입하지 않은 경우
        if (!StringUtils.hasText(oauth2UserInfo.getEmail())) {
            return true;
        }
        return false;
    }

    /**
     * - 전화번호가 같고 프로바이더가 다른 경우
     * - 연령대, 생일 등 개인 정보가 변경된 경우
     * @param user
     * @param oauth2UserInfo
     * @return
     */
    private boolean requiresUpdateUser(User user, OAuth2UserInfo oauth2UserInfo, String registrationId) {
        // TODO : 유저 정보 업데이트 필요 로직
        return false;

    }

    private User updateUser(User existingUser, OAuth2UserInfo oauth2UserInfo) {
        // TODO: requiresUpdateUser에서 요청될 업데이트 로직
        return userRepository.save(existingUser);
    }

    private User registerUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oauth2UserInfo) {
        MatgptOAuth2Provider provider = MatgptOAuth2Provider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase());

        User user = User.builder()
                .name(UUID.randomUUID().toString()) // TODO: 자동으로 생성할 닉네임 구현
                .email(oauth2UserInfo.getEmail())
                .gender(oauth2UserInfo.getGender())
                .ageGroup(oauth2UserInfo.getAgeGroup())
                .provider(provider)
                .providerId(oauth2UserInfo.getId())
                .build();

        return userRepository.save(user);
    }

}