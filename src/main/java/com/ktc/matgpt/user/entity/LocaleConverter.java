package com.ktc.matgpt.user.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Locale;

@Converter
public class LocaleConverter implements AttributeConverter<Locale, String> {
    @Override
    public String convertToDatabaseColumn(Locale locale) {
        return (locale == null) ? null : locale.toString();
    }

    @Override
    public Locale convertToEntityAttribute(String localeString) {
        return (localeString == null) ? null : new Locale.Builder().setLanguageTag(localeString).build();
    }
}
