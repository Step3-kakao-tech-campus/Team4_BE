package com.ktc.matgpt.chatgpt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {

    public static final String API_URL = "https://api.openai.com/v1/chat/completions";
    public static final Duration API_RESPONSE_TIMEOUT = Duration.ofSeconds(60);

    @Value("${chatgpt.api.key}")
    private String apiKey;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(API_RESPONSE_TIMEOUT) // 연결 타임아웃 설정
                .setReadTimeout(API_RESPONSE_TIMEOUT) // 읽기 타임아웃 설정

                .additionalInterceptors((request, body, execution) -> {
                    HttpHeaders headers = request.getHeaders();
                    headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey);
                    headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                    return execution.execute(request, body);
                })
                .build();
    }
}
