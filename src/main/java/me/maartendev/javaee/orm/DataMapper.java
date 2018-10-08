package me.maartendev.javaee.orm;

import me.maartendev.javaee.config.DatabaseProperties;
import me.maartendev.javaee.utilities.ClassInspector;
import org.mariadb.jdbc.Driver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class DataMapper {
    private Connection connection = null;
    private PreparedStatement statement = null;
    private ResultSet resultSet = null;

    public DataMapper() {
        try {
            DriverManager.registerDriver((Driver) Class.forName(DatabaseProperties.getDriver()).getDeclaredConstructor().newInstance());
            connection = DriverManager.getConnection(DatabaseProperties.getDns());
        } catch (SQLException | InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ResultSet runQuery(String query) {
        try {
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            return resultSet;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public <T> List<T> all(Class<T> instanceClass) {
        String table = this.getTableName(instanceClass);
        String query = "SELECT * FROM " + table;

        return this.fetchCollection(instanceClass, query);
    }

    private <T> List<T> fetchCollection(Class<T> instanceClass, String query) {
        List<T> models = new ArrayList<>();
        this.resultSet = runQuery(query);

        try {
            while (this.resultSet.next()) {
                models.add(this.createInstanceWithDataSourceData(instanceClass, this.resultSet));
            }

            return models;
        } catch (SQLException e) {
            return null;
        }
    }

    public <T> T find(Class<T> instanceClass, int id) {
        this.resultSet = runQuery("SELECT * FROM " + this.getTableName(instanceClass) + " WHERE id=" + id);

        try {
            this.resultSet.next();
            return this.createInstanceWithDataSourceData(instanceClass, this.resultSet);
        } catch (SQLException e) {
            return null;
        }
    }

    private String getTableName(Class<?> clazz) {
        return this.getAsNoun(clazz).toLowerCase() + "s";
    }

    private String getAsNoun(Class<?> clazz) {
        return clazz.getName()
                .substring(this.getClass().getName().lastIndexOf(".") + 1)
                .replace("DTO", "");
    }

    private <T> T createInstanceWithDataSourceData(Class<T> instanceClass, ResultSet resultSet) throws SQLException {
        List<Method> setters = ClassInspector.getSetters(instanceClass);

        try {
            T instance = instanceClass.newInstance();

            for (Method setter : setters) {
                String setterMethod = setter.getName();
                String setterProperty = setterMethod.replace("set", "");
                String propertyName = setterProperty.substring(0, 1).toLowerCase() + setterProperty.substring(1);


                Object propertyValue = null;
                Class<?> propertyType = setter.getParameterTypes()[0];

                if (propertyType == int.class) {
                    propertyValue = resultSet.getInt(propertyName);
                } else if (propertyType == String.class) {
                    propertyValue = resultSet.getString(propertyName);
                } else if (propertyType == boolean.class) {
                    propertyValue = resultSet.getBoolean(propertyName);
                } else if (propertyType == List.class) {
                    String relatedEntityType = ((ParameterizedType) setter.getGenericParameterTypes()[0]).getActualTypeArguments()[0].getTypeName();
                    Class<?> relationClass = Class.forName(relatedEntityType);

                    String joinTable = this.getAsNoun(instanceClass) + "_" + this.getAsNoun(relationClass);

                    String query = "SELECT * FROM " + joinTable + " LEFT JOIN " + this.getTableName(relationClass) + " ON " +
                            this.getAsNoun(instanceClass) + "_id=";

                    // SELECT * FROM playlist_track LEFT JOIN tracks ON playlist_track.track_id=tracks.id WHERE playlist_track.playlist_id=id

                    propertyValue = this.fetchCollection(relationClass, query);
                }

                if (propertyValue != null) {
                    instance.getClass().getMethod(setterMethod, propertyType).invoke(instance, propertyValue);
                }
            }

            return instance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void close(Connection conn, Statement ps, ResultSet res) {
        if (conn != null) try {
            conn.close();
        } catch (SQLException ignored) {
        }
        if (ps != null) try {
            ps.close();
        } catch (SQLException ignored) {
        }
        if (res != null) try {
            res.close();
        } catch (SQLException ignored) {
        }
    }
}
