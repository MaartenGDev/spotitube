package me.maartendev.spotitube.producers;

import javax.enterprise.inject.Produces;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class FileConfigPropertiesProducer {
    private static final Logger LOGGER = Logger.getLogger(FileConfigPropertiesProducer.class.getName());

    @Produces
    public Properties getProperties(){
        Properties properties = new Properties();

        try {
            InputStream propertyStream = getClass().getClassLoader().getResourceAsStream("database.properties");

            if(propertyStream == null){
                return properties;
            }

            properties.load(propertyStream);
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }

        return properties;
    }

}
