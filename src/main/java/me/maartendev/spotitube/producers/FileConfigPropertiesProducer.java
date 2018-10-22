package me.maartendev.spotitube.producers;

import javax.enterprise.inject.Produces;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class FileConfigPropertiesProducer {
    private static final Logger LOGGER = Logger.getLogger(FileConfigPropertiesProducer.class.getName());

    @Produces
    public Properties getProperties(){
        Properties properties = new Properties();

        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("database.properties"));
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }

        return properties;
    }

}
