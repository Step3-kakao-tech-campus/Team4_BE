package com.ktc.matgpt.user.entity;

import com.ktc.matgpt.security.oauth2.MatgptOAuth2Provider;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_tb")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Email
    @Column(nullable = false)
    private String email;

    @Column(nullable = true)
    private String profileImageUrl;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = true)
    private AgeGroup ageGroup;

    @Column(nullable = true)
    private Boolean emailVerified = false;

    @NotNull
    @Enumerated(EnumType.STRING)
    private MatgptOAuth2Provider provider;

    private String providerId;

}
