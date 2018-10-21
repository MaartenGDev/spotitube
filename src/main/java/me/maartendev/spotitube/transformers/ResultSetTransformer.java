package me.maartendev.spotitube.transformers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface ResultSetTransformer<T> {
    List<T> convertToList(ResultSet resultSet) throws SQLException;
}
