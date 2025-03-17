package com.nubo.masterparticipant.api.application.security;

import com.nubo.masterparticipant.api.application.filters.GlobalFilter;
import com.nubo.masterparticipant.api.application.properties.PropertiesConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomBasicAuthenticationEntryPoint customBasicAuthenticationEntryPoint;
    private final CustomBearerTokenAuthenticationEntryPoint customBearerTokenAuthenticationEntryPoint;
    private final CustomBearerTokenAccessDeniedHandler customBearerTokenAccessDeniedHandler;
    private final GlobalFilter globalFilter;
    private final PropertiesConfig propertiesConfig;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(globalFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html",
                                "/v1/keycloak/token")
                        .permitAll().anyRequest().authenticated())
                .oauth2ResourceServer(oauth ->
                        oauth.opaqueToken(opaqueToken ->
                                        opaqueToken
                                                .introspectionUri(propertiesConfig.getKeycloakIntrospectionUri())
                                                .introspectionClientCredentials(propertiesConfig.getKeycloakClientId(),
                                                        propertiesConfig.getKeycloakClientSecret())
                                )
                                .authenticationEntryPoint(this.customBearerTokenAuthenticationEntryPoint)
                                .accessDeniedHandler(this.customBearerTokenAccessDeniedHandler)
                )
                .cors(Customizer.withDefaults())
                .httpBasic(httpBasic -> httpBasic.authenticationEntryPoint(this.customBasicAuthenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

}

