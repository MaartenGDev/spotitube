package me.maartendev.spotitube.transformers;

import java.sql.ResultSet;

public interface ResultSetTransformer<T> {
    T transform(ResultSet resultSet);
}
