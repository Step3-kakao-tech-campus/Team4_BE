package com.ktc.matgpt.utils.converter;


import com.ktc.matgpt.domain.user.entity.LocaleEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


//deploy 환경에서 성능 향상을 위해 사용 (개발환경에는 toString으로 저장)
@Converter(autoApply = true)
public class LocaleEnumConverter implements AttributeConverter<LocaleEnum, String> {

    @Override
    public String convertToDatabaseColumn(LocaleEnum attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.toString();
    }

    @Override
    public LocaleEnum convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return LocaleEnum.valueOf(dbData.toUpperCase());
    }
}
