package com.nubo.masterparticipant.api.application.database;

import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class NamingStrategyConfigTest {

    @Test
    @DisplayName("Physical Naming Strategy Bean")
    void physicalNamingStrategyBeanTest() {
        ApplicationContext context = new AnnotationConfigApplicationContext(NamingStrategyConfig.class);

        PhysicalNamingStrategy namingStrategy = context.getBean(PhysicalNamingStrategy.class);

        assertNotNull(namingStrategy);

        assertInstanceOf(UpperNamingConfig.class, namingStrategy);
    }

}
