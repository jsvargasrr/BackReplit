package com.nubo.masterparticipant.api.application.database;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class UpperNamingConfigTest {

    private UpperNamingConfig upperNamingConfig;

    @BeforeEach
    void setUp() {
        upperNamingConfig = new UpperNamingConfig();
    }

    @Test
    @DisplayName("To Physical CatalogName")
    void toPhysicalCatalogNameTest() {
        JdbcEnvironment context = mock(JdbcEnvironment.class);
        Identifier input = new Identifier("catalogName", false);
        Identifier expected = new Identifier("CATALOGNAME", false);

        Identifier result = upperNamingConfig.toPhysicalCatalogName(input, context);

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("To Physical SchemaName")
    void toPhysicalSchemaNameTest() {
        JdbcEnvironment context = mock(JdbcEnvironment.class);
        Identifier input = new Identifier("schemaName", false);
        Identifier expected = new Identifier("SCHEMANAME", false);

        Identifier result = upperNamingConfig.toPhysicalSchemaName(input, context);

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("To Physical SequenceName")
    void toPhysicalSequenceNameTest() {
        JdbcEnvironment context = mock(JdbcEnvironment.class);
        Identifier input = new Identifier("sequenceName", false);
        Identifier expected = new Identifier("SEQUENCENAME", false);

        Identifier result = upperNamingConfig.toPhysicalSequenceName(input, context);

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("To Physical TableName")
    void toPhysicalTableNameTest() {
        JdbcEnvironment context = mock(JdbcEnvironment.class);
        Identifier input = new Identifier("tableName", false);
        Identifier expected = new Identifier("TABLENAME", false);

        Identifier result = upperNamingConfig.toPhysicalTableName(input, context);

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("To Physical ColumnName")
    void toPhysicalColumnNameTest() {
        JdbcEnvironment context = mock(JdbcEnvironment.class);
        Identifier input = new Identifier("columnName", false);
        Identifier expected = new Identifier("COLUMNNAME", false);

        Identifier result = upperNamingConfig.toPhysicalColumnName(input, context);

        assertEquals(expected, result);
    }

}