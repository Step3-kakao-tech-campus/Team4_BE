package com.ktc.matgpt.security.oauth2.userInfo;

import com.ktc.matgpt.user.entity.AgeGroup;
import com.ktc.matgpt.user.entity.Gender;

import java.util.Map;


public abstract class OAuth2UserInfo {
    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public abstract String getId();

    public abstract String getEmail();

    public abstract Gender getGender();

    public abstract AgeGroup getAgeGroup();
}