package com.ktc.matgpt.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "RefreshToken")
public class RefreshToken {

    @Id
    @Column(name = "refresh_key")
    private String key;

    @Column(name = "refresh_value")
    private String value;

    public RefreshToken(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Builder
    public static RefreshToken create(String key, String value){
        return new RefreshToken(key,value);
    }

    public RefreshToken updateValue(String token) {
        this.value = token;
        return this;
    }
}
