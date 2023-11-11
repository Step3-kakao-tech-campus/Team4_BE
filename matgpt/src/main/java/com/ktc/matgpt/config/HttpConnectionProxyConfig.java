package com.ktc.matgpt.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;

@Slf4j
@Profile(value = {"prod"})
@Configuration
public class HttpConnectionProxyConfig {

    private static final String PROXY_HOST = "krmp-proxy.9rum.cc";
    private static final int PROXY_PORT = 3128;
    public static final Duration API_RESPONSE_TIMEOUT = Duration.ofSeconds(60);

    @Value("${chatgpt.api.key}")
    private String chatGptApiKey;

    @Bean
    public RestTemplate restTemplateWithGlobalProxy(RestTemplateBuilder builder) {
        SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_HOST, PROXY_PORT));
        clientHttpRequestFactory.setProxy(proxy);

        // Set timeouts
        clientHttpRequestFactory.setConnectTimeout((int) API_RESPONSE_TIMEOUT.toMillis());
        clientHttpRequestFactory.setReadTimeout((int) API_RESPONSE_TIMEOUT.toMillis());

        return builder
                .requestFactory(() -> clientHttpRequestFactory)
                .additionalInterceptors((request, body, execution) -> {
                    HttpHeaders headers = request.getHeaders();

                    log.info("apiKey : " + chatGptApiKey);

                    // Add the Authorization header only for requests to the OpenAI API
                    if (request.getURI().getHost().equals("api.openai.com")) {
                        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + chatGptApiKey);
                    }

                    headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                    return execution.execute(request, body);
                })
                .build();
    }
}