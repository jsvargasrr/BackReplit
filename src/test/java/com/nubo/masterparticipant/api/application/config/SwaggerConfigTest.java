package com.nubo.masterparticipant.api.application.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SwaggerConfigTest {

    private SwaggerConfig swaggerConfig;
    private GlobalOpenApiCustomizer customizer;

    @BeforeEach
    public void setUp() {
        swaggerConfig = new SwaggerConfig();
    }

    @Test
    @DisplayName("Index Test")
    void indexTest() {
        ModelAndView modelAndView = swaggerConfig.index();
        assertNotNull(modelAndView);
        assertEquals("redirect:/swagger-ui/index.html", modelAndView.getViewName());
    }

    @Test
    @DisplayName("Public API Test")
    void publicApiTest() {
        String appGroup = "public-group";
        GroupedOpenApi groupedOpenApi = swaggerConfig.publicApi(appGroup);
        assertNotNull(groupedOpenApi);
        assertEquals(appGroup, groupedOpenApi.getGroup());
        assertEquals("/**", groupedOpenApi.getPathsToMatch().get(0));
    }

    @Test
    @DisplayName("Custom OpenAPI Test")
    void customOpenAPITest() {
        String appTitle = "API Title";
        String appDescription = "API Description";
        String appVersion = "1.0.0";

        OpenAPI openAPI = swaggerConfig.customOpenAPI(appTitle, appDescription, appVersion);

        assertNotNull(openAPI);
        assertEquals(appTitle, openAPI.getInfo().getTitle());
        assertEquals(appDescription, openAPI.getInfo().getDescription());
        assertEquals(appVersion, openAPI.getInfo().getVersion());

        SecurityScheme securityScheme = openAPI.getComponents().getSecuritySchemes().get("bearerAuth");
        assertNotNull(securityScheme);
        assertEquals("bearerAuth", securityScheme.getName());
        assertEquals(SecurityScheme.Type.HTTP, securityScheme.getType());
        assertEquals("bearer", securityScheme.getScheme());
        assertEquals("JWT", securityScheme.getBearerFormat());
    }

}
