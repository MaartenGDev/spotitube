package me.maartendev.spotitube.config;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Properties;

public class DatabaseProperties {
    private Properties properties;

    @Inject
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public DatabaseProperties() { }

    public String getDriver() {
        return this.getPropertyValue("DRIVER");
    }

    public String getDsn() {
        return this.getPropertyValue("DSN");
    }

    private String getPropertyValue(String key) {
        String environmentValue = System.getenv(key);

        if (environmentValue != null) {
            return environmentValue;
        }

        return this.properties.getProperty(key);
    }
}
