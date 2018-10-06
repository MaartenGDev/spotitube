package me.maartendev.javaee.dao;

import me.maartendev.javaee.config.DatabaseProperties;
import me.maartendev.javaee.utilities.ClassInspector;
import org.mariadb.jdbc.Driver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class DAO {
    private Connection connection = null;
    private PreparedStatement statement = null;
    private ResultSet resultSet = null;

    public DAO() {
        try {
            DriverManager.registerDriver((Driver) Class.forName(DatabaseProperties.getDriver()).getDeclaredConstructor().newInstance());
            connection = DriverManager.getConnection(DatabaseProperties.getDns());
        } catch (SQLException | InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ResultSet runQuery(String query) {
        try {
            statement = connection.prepareStatement("SELECT * FROM playlists");
            resultSet = statement.executeQuery();

            return resultSet;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public <T> List<T> all(Class<T> instanceClass) {
        this.resultSet = runQuery("SELECT * FROM " + this.getTableName());

        List<T> models = new ArrayList<>();

        try {
            while (this.resultSet.next()) {
                models.add(this.getInstance(instanceClass, this.resultSet));
            }

            return models;
        } catch (SQLException e) {
            return null;
        }
    }

    public <T> T find(Class<T> instanceClass, int id) {
        this.resultSet = runQuery("SELECT * FROM " + this.getTableName() + " WHERE id=2");

        try {
            this.resultSet.next();

            return this.getInstance(instanceClass, this.resultSet);
        } catch (SQLException e) {
            return null;
        }
    }

    private String getTableName(){
        return this.getClass().getName().substring(this.getClass().getName().lastIndexOf(".") + 1).replace("DAO", "").toLowerCase() + "s";
    }

    private <T> T getInstance(Class<T> instanceClass, ResultSet resultSet) throws SQLException {
        List<Method> setters = ClassInspector.getSetters(instanceClass);
        T instance = null;

        try {
            instance = instanceClass.newInstance();


            for (Method setter : setters) {
                String setterMethod = setter.getName();
                String setterProperty = setterMethod.replace("set", "");
                String propertyName = setterProperty.substring(0, 1).toLowerCase() + setterProperty.substring(1);


                Object propertyValue = null;
                Class<?> propertyType;

                try {
                    propertyType = instance.getClass().getMethod(setterMethod.replace("set", "get")).getReturnType();
                } catch (NoSuchMethodException e) {
                    propertyType = instance.getClass().getMethod(setterMethod.replace("set", "is")).getReturnType();
                }

                if (propertyType == int.class) {
                    propertyValue = resultSet.getInt(propertyName);
                } else if (propertyType == String.class) {
                    propertyValue = resultSet.getString(propertyName);
                } else if (propertyType == boolean.class) {
                    propertyValue = resultSet.getBoolean(propertyName);
                }

                if (propertyValue != null) {
                    instance.getClass().getMethod(setterMethod, propertyType).invoke(instance, propertyValue);
                }
            }

            return instance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
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
