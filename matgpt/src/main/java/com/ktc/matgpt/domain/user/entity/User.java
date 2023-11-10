package com.ktc.matgpt.domain.user.entity;

import com.ktc.matgpt.utils.converter.LocaleEnumConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Locale;


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

    @Column(nullable = true)
    private String name; //닉네임

    @Column(nullable = false)
    private String email; //아이디

    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    private String profileImageUrl;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = true)
    private AgeGroup ageGroup;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private LocaleEnum locale;


}
