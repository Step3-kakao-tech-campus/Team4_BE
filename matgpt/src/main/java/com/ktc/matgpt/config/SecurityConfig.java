package com.ktc.matgpt.config;

import com.ktc.matgpt.security.jwt.JwtAccessDeniedHandler;
import com.ktc.matgpt.security.jwt.JwtAuthenticationEntryPoint;
import com.ktc.matgpt.security.jwt.JwtAuthenticationFilter;
import com.ktc.matgpt.security.oauth2.MatgptOAuth2Provider;
import com.ktc.matgpt.security.oauth2.MatgptOAuth2UserService;
import com.ktc.matgpt.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.ktc.matgpt.security.oauth2.OAuth2AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private static final List<String> clients = List.of("kakao", "google");
    private static final String CLIENT_PROPERTY_KEY = "spring.security.oauth2.client.registration.";

    private final Environment env;
    private final MatgptOAuth2UserService oauthUserService;
    private final OAuth2AuthenticationSuccessHandler oauthSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oauthFailureHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    // Handler 추가
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        List<ClientRegistration> registrations = clients.stream()
                .map(this::getRegistration)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new InMemoryClientRegistrationRepository(registrations);
    }

    private ClientRegistration getRegistration(String client) {
        String clientId = env.getProperty(CLIENT_PROPERTY_KEY + client + ".client-id");

        if (clientId == null) {
            return null;
        }

        String clientSecret = env.getProperty(CLIENT_PROPERTY_KEY + client + ".client-secret");

        if (client.equals("google")) {
            return MatgptOAuth2Provider.GOOGLE.getBuilder(client)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .build();
        }

        if (client.equals("kakao")) {
            return MatgptOAuth2Provider.KAKAO.getBuilder(client)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .build();
        }

        return null;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CSRF 설정 Disable
        http.csrf(
                csrfCustomizer -> csrfCustomizer
                        .ignoringRequestMatchers(antMatcher("/h2-console/**"))
                        .disable()
                // TODO
        );
        // 헤더 설정
        http.headers(
                // h2-console에서 iframe을 사용함. X-Frame-Options을 위해 sameOrigin 설정
                headersCustomizer -> headersCustomizer
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
        );
        // 인증 설정
        http.authorizeHttpRequests(
                authorizeCustomizer -> authorizeCustomizer
                        .requestMatchers(antMatcher("/h2-console/**")).permitAll()
                        .requestMatchers(antMatcher("/auth/**")).permitAll()
                        .requestMatchers(antMatcher("/stores/**")).permitAll()
                        .requestMatchers(antMatcher("/swagger-ui/**")).permitAll()
                        .requestMatchers(antMatcher("/v3/api-docs/")).permitAll()

                        .anyRequest().permitAll() // TODO
        );
//---------------------------------------------
        http.exceptionHandling(
                exceptionHandler -> exceptionHandler.accessDeniedHandler(
                        (request, response, accessDeniedException) ->
                                jwtAccessDeniedHandler.handle(request, response, accessDeniedException)
                )
        );

        http.exceptionHandling(
                exceptionHandler -> exceptionHandler.authenticationEntryPoint(
                        (request, response, authException) ->
                                jwtAuthenticationEntryPoint.commence(request, response, authException)
                )
        );
//---------------------------------------------

        http.sessionManagement(
                sessionCustomizer -> sessionCustomizer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.oauth2Login(
                oauthCustomizer -> oauthCustomizer
                        .userInfoEndpoint(
                                endpointCustomizer -> endpointCustomizer
                                        .userService(oauthUserService)
                        )
                        .successHandler(oauthSuccessHandler)
                        .failureHandler(oauthFailureHandler)
        );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
