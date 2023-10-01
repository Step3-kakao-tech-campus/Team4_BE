package com.ktc.matgpt.security;

import com.ktc.matgpt.security.oauth2.userInfo.OAuth2UserInfo;
import com.ktc.matgpt.user.entity.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@Getter
public class UserPrincipal implements OAuth2User, UserDetails {
    private final Long id;
    //name은 여기서 생성
    private final String email;
    private final Gender gender;
    private final AgeGroup ageGroup;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(Long id, String email, Gender gender, AgeGroup ageGroup, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.gender = gender;
        this.ageGroup = ageGroup;
        this.authorities = authorities;
    }

    @Builder
    public static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority("ROLE_GUEST"));

        return new UserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getGender(),
                user.getAgeGroup(),
                authorities
        );
    }

    public static UserPrincipal create(User user, OAuth2UserInfo oAuth2UserInfo) {
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        return userPrincipal;
    }


    //For UserDetails
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return String.valueOf(id);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    //For AuthenticatedPrincipal
    @Override
    public String getName() {
        return String.valueOf(id);
    }
    @Override
    public Map<String, Object> getAttributes() {
        return new HashMap<>();
    }

}