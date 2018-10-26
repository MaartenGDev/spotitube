package me.maartendev.spotitube.factories;

import me.maartendev.spotitube.config.DatabaseProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Properties;

public class DataSourceProducerTest {
    @Test
    public void testShouldReturnDataSourceConfiguredUsingDataProperties(){
        DataSourceFactory dataSourceFactory = new DataSourceFactory();

        DatabaseProperties databaseProperties = Mockito.mock(DatabaseProperties.class);
        Mockito.when(databaseProperties.getDriver()).thenReturn("jdbc://example");
        Mockito.when(databaseProperties.getDsn()).thenReturn("com.example.Driver");

        databaseProperties.setProperties(new Properties());

        dataSourceFactory.setDatabaseProperties(databaseProperties);

        Assertions.assertNotNull(dataSourceFactory.build());
    }
}
