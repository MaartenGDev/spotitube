package me.maartendev.spotitube.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Properties;

public class DatabasePropertiesTest {
    @Test
    public void testShouldReturnDataSourceConfiguredUsingDataProperties(){
        String driverClass = "com.example.Driver";
        String dsn = "com.example.Driver";
        Properties properties = new Properties();
        properties.setProperty("DRIVER", driverClass);
        properties.setProperty("DSN", dsn);

        DatabaseProperties databaseProperties = new DatabaseProperties();
        databaseProperties.setProperties(properties);

        Assertions.assertEquals(driverClass, databaseProperties.getDriver());
        Assertions.assertEquals(dsn, databaseProperties.getDsn());
    }
}
