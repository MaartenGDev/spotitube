package me.maartendev.spotitube.producers;

import me.maartendev.spotitube.config.DatabaseProperties;
import org.apache.commons.dbcp.BasicDataSource;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.sql.DataSource;

public class DataSourceProducer {
    private DatabaseProperties databaseProperties;

    @Inject
    public void setDatabaseProperties(DatabaseProperties databaseProperties) {
        this.databaseProperties = databaseProperties;
    }

    @Produces
    public DataSource getDataSource(){
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(this.databaseProperties.getDriver());
        dataSource.setUrl(this.databaseProperties.getDsn());

        return dataSource;
    }

}
