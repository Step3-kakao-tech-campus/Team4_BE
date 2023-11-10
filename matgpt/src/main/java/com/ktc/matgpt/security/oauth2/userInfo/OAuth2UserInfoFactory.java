package com.ktc.matgpt.security.oauth2.userInfo;


import com.ktc.matgpt.exception.auth.Oauth2ProcessingException;
import com.ktc.matgpt.security.oauth2.MatgptOAuth2Provider;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class OAuth2UserInfoFactory {

    private static final Map<String, Function<Map<String, Object>, OAuth2UserInfo>> userInfoSuppliers = new HashMap<>();


    static {
        userInfoSuppliers.put(MatgptOAuth2Provider.KAKAO.toString().toLowerCase(), KakaoOAuth2UserInfo::new);
        userInfoSuppliers.put(MatgptOAuth2Provider.GOOGLE.toString().toLowerCase(), GoogleOAuth2UserInfo::new);
    }

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        Function<Map<String, Object>, OAuth2UserInfo> supplier = userInfoSuppliers.get(registrationId.toLowerCase());
        if (supplier != null) {
            return supplier.apply(attributes);
        } else {
            throw new Oauth2ProcessingException();
        }
    }
}