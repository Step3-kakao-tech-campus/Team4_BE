package com.ktc.matgpt.security.oauth2.userInfo;


import com.ktc.matgpt.user.entity.AgeGroup;
import com.ktc.matgpt.user.entity.Gender;

import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public Gender getGender() {
        return null;
    }// TODO : 구글 형식으로 변환
    @Override
    public AgeGroup getAgeGroup(){
        return null;
    }// TODO : 구글 형식으로 변환

}
