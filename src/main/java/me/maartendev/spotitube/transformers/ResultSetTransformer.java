package me.maartendev.spotitube.transformers;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetTransformer<T> {
    T transform(ResultSet resultSet) throws SQLException;
}
