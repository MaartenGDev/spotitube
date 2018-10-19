package me.maartendev.spotitube.dao;

import me.maartendev.spotitube.config.DatabaseProperties;
import me.maartendev.spotitube.transformers.ResultSetTransformer;
import org.mariadb.jdbc.Driver;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class DAO {
    private static final Logger LOGGER = Logger.getLogger(DAO.class.getName());


    public DAO() {
        try {
            DriverManager.registerDriver((Driver) Class.forName(DatabaseProperties.getDriver()).getDeclaredConstructor().newInstance());
        } catch (SQLException | InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }

    public ResultSet runQuery(String query) {
        return this.runQuery(query, new HashMap<>());
    }

    public ResultSet runQuery(String query, Map<Integer, Object> bindings) {
        ResultSet resultSet = null;
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DatabaseProperties.getDns());
            statement = connection.prepareStatement(query);

            for (Map.Entry<Integer, Object> binding : bindings.entrySet()) {
                if (binding.getValue() instanceof String) {
                    statement.setString(binding.getKey(), (String) binding.getValue());
                } else if (binding.getValue() instanceof Integer) {
                    statement.setInt(binding.getKey(), (Integer) binding.getValue());
                } else if (binding.getValue() instanceof Boolean) {
                    statement.setBoolean(binding.getKey(), (Boolean) binding.getValue());
                }
            }

            resultSet = statement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error:" + e.getMessage());
        } finally {
            close(connection, statement, resultSet);
        }

        return null;
    }
    protected <K> K fetchResultForQuery(String query, ResultSetTransformer<K> transformer, Map<Integer, Object> bindings) {
        List<K> results = this.fetchResultsForQuery(query, transformer, bindings);
        return results.size() > 0 ? results.get(0) : null;
    }

    protected <K> List<K> fetchResultsForQuery(String query, ResultSetTransformer<K> transformer) {
        return this.fetchResultsForQuery(query, transformer, new HashMap<>());
    }

    protected  <K> List<K> fetchResultsForQuery(String query, ResultSetTransformer<K> transformer, Map<Integer, Object> bindings) {
        ResultSet resultSet = this.runQuery(query, bindings);
        List<K> models = new ArrayList<>();

        try {
            while (resultSet.next()) {
                models.add(transformer.transform(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return models;
    }



    private void close(Connection conn, Statement ps, ResultSet res) {
        if (conn != null) try {
            conn.close();
        } catch (SQLException ignored) {
            LOGGER.log(Level.INFO, "Failed closing connection");
        }
        if (ps != null) try {
            ps.close();
        } catch (SQLException ignored) {
            LOGGER.log(Level.INFO, "Failed closing Prepared Statement");
        }
        if (res != null) try {
            res.close();
        } catch (SQLException ignored) {
            LOGGER.log(Level.INFO, "Failed closing ResultSet");
        }
    }
}
