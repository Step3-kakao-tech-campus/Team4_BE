package com.ktc.matgpt.security.oauth2.userInfo;

import com.ktc.matgpt.user.entity.AgeGroup;
import com.ktc.matgpt.user.entity.Gender;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    private Map<String, Object> getAccount() {
        return (Map<String, Object>) getAttributes().get("kakao_account");
    }

    @Override
    public String getId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getEmail() {
        return (String) getAccount().get("email");
    }

    @Override
    public Gender getGender() {
        String gender = (String) getAccount().get("gender");

        return switch (gender) {
            case "male" -> Gender.MALE;
            case "female" -> Gender.FEMALE;
            default -> throw new OAuth2AuthenticationException("Unprovided gender");
        };
    }


    @Override
    public AgeGroup getAgeGroup(){
        String ageRange = (String) getAccount().get("age_range");
        return AgeGroup.of(ageRange, "~");
    }

}
