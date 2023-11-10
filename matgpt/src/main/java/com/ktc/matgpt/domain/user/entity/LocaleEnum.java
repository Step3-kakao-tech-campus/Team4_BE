package com.ktc.matgpt.domain.user.entity;

import lombok.Getter;

import java.util.Locale;

@Getter
public enum LocaleEnum {
    CHINESE(Locale.CHINESE, "중국어", "중국"),
    SIMPLIFIED_CHINESE(Locale.SIMPLIFIED_CHINESE, "중국어(간체)", "중국(간체)"),
    TRADITIONAL_CHINESE(Locale.TRADITIONAL_CHINESE, "중국어(번체)", "중국(번체)"),
    FRANCE(Locale.FRANCE, "프랑스어", "프랑스"),
    GERMANY(Locale.GERMANY, "독일어", "독일"),
    ITALY(Locale.ITALY, "이탈리아어", "이탈리아"),
    JAPAN(Locale.JAPAN, "일본어", "일본"),
    KOREA(Locale.KOREA, "한국어", "한국"),
    UK(Locale.UK, "영국어", "영국"),
    US(Locale.US, "미국어", "미국"),
    CANADA(Locale.CANADA, "캐나다어", "캐나다"),
    CANADA_FRENCH(Locale.CANADA_FRENCH, "캐나다 프랑스어", "캐나다(프랑스어)");

    private final Locale locale;
    private final String languageDescription;
    private final String countryDescription;

    LocaleEnum(Locale locale, String languageDescription, String countryDescription) {
        this.locale = locale;
        this.languageDescription = languageDescription;
        this.countryDescription = countryDescription;
    }

    public static LocaleEnum fromString(String localeStr) {
        for (LocaleEnum localeEnum : LocaleEnum.values()) {
            if (localeEnum.name().equalsIgnoreCase(localeStr)) {
                return localeEnum;
            }
        }
        return null; // 혹은 기본값 반환
    }
}
