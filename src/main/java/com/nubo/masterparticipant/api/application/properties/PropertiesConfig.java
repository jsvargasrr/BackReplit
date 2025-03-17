package com.nubo.masterparticipant.api.application.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class PropertiesConfig {

    @Value("${spring.application.name}")
    private String apiName;

    @Value("${config.application.rateLimited.number-max-request}")
    private Integer numberMaxRequest;

    @Value("${config.application.rateLimited.range-time-request}")
    private Integer rangeTimeRequest;

    @Value("${config.application.rateLimited.waiting-time}")
    private Integer waitingTime;

    @Value("${client.keycloak.client-id}")
    private String keycloakClientId;

    @Value("${client.keycloak.client-secret}")
    private String keycloakClientSecret;

    @Value("${client.keycloak.introspection-uri}")
    private String keycloakIntrospectionUri;

}
