package com.nubo.masterparticipant.api.application.config;

import com.nubo.masterparticipant.api.application.interceptors.GlobalInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class WebConfigTest {

    @Mock
    private GlobalInterceptor globalInterceptor;

    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.registerBean(GlobalInterceptor.class, () -> globalInterceptor);
        context.register(WebConfig.class);
        context.refresh();

        applicationContext = context;
    }

    @Test
    @DisplayName("Locale Resolver bean is not null")
    void localeResolverBeanIsNotNullTest() {
        LocaleResolver localeResolver = applicationContext.getBean(LocaleResolver.class);
        assertNotNull(localeResolver);
    }

    @Test
    @DisplayName("Locale Resolver default locale is English")
    void localeResolverDefaultLocaleIsEnglishTest() {
        LocaleResolver localeResolver = applicationContext.getBean(LocaleResolver.class);
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        Locale defaultLocale = localeResolver.resolveLocale(mockRequest);

        assertNotNull(defaultLocale);
        assertEquals(Locale.ENGLISH, defaultLocale);
    }

}