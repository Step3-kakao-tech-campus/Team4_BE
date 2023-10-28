package com.ktc.matgpt.i18n;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class LocaleService {

    private final MessageSource messageSource;

    public String getMessage(String key, Locale locale) {
        return messageSource.getMessage(key, null, locale);
    }

    public void changeLocale(String newLocale, HttpServletRequest request, HttpServletResponse response) {
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
        if (localeResolver != null) {
            localeResolver.setLocale(request, response, StringUtils.parseLocaleString(newLocale));
        }
    }
}