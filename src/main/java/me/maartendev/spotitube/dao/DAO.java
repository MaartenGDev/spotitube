package me.maartendev.spotitube.dao;

import me.maartendev.spotitube.transformers.ResultSetRowTransformer;
import me.maartendev.spotitube.transformers.ResultSetTransformer;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class DAO {
    private DataSource dataSource;

    private <K> ResultSetTransformer<K> buildResultSetTransformer(ResultSetRowTransformer<K> resultSetRowTransformer) {
        return resultSet -> {
            List<K> results = new ArrayList<>();

            while (resultSet.next()) {
                results.add(resultSetRowTransformer.transform(resultSet));
            }

            return results;
        };
    }

    @Inject
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void runQuery(String query, List bindings) throws SQLException {
        this.executeQuery(query, bindings, null);
    }

    protected <K> K fetchResultForQuery(String query, ResultSetRowTransformer<K> transformer) {
        return this.fetchResultsForQuery(query, transformer, new ArrayList<>()).get(0);
    }

    protected <K> K fetchResultForQuery(String query, ResultSetRowTransformer<K> transformer, List bindings) {
        List<K> results = this.fetchResultsForQuery(query, transformer, bindings);

        return results.size() > 0 ? results.get(0) : null;
    }

    protected <K> List<K> fetchResultsForQuery(String query, ResultSetRowTransformer<K> transformer) {
        return this.fetchResultsForQuery(query, transformer, new ArrayList<>());
    }

    public <K> List<K> fetchResultsForQuery(String query, ResultSetRowTransformer<K> transformer, List bindings) {
        try {
            return this.executeQuery(query, bindings, this.buildResultSetTransformer(transformer));
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }


    private <K> List<K> executeQuery(String query, List bindings, ResultSetTransformer<K> resultSetTransformer) throws SQLException {
        query = addPlaceholdersForBindings(query, bindings);
        try (Connection con = this.dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(query)) {
                addBindingsToStatement(bindings, statement, 1);

                boolean queryHasSideEffects = resultSetTransformer == null;

                if (queryHasSideEffects) {
                    statement.execute();
                    statement.getGeneratedKeys();
                    return new ArrayList<>();
                }

                try (ResultSet rs = statement.executeQuery()) {
                    return resultSetTransformer.convertToList(rs);
                }
            }
        }
    }

    private String addPlaceholdersForBindings(String query, List bindings) {
        int bindingPosition = 1;

        for (Object binding : bindings) {
            int placeholderIndex = StringUtils.ordinalIndexOf(query, "?", bindingPosition);
            if (binding instanceof List) {
                List entries = (List) binding;

                String placeholders = String.join(",", Collections.nCopies(entries.size(), "?"));

                query = query.substring(0, placeholderIndex) + placeholders + query.substring(placeholderIndex + 1);
            }

            bindingPosition++;
        }

        return query;
    }

    private void addBindingsToStatement(List bindings, PreparedStatement statement, int bindingPositionOffset) throws SQLException {
        for (Object binding : bindings) {
            if (binding instanceof String) {
                statement.setString(bindingPositionOffset, (String) binding);
            } else if (binding instanceof Integer) {
                statement.setInt(bindingPositionOffset, (Integer) binding);
            } else if (binding instanceof Boolean) {
                statement.setBoolean(bindingPositionOffset, (Boolean) binding);
            } else if (binding instanceof List) {
                this.addBindingsToStatement((List) binding, statement, bindingPositionOffset);
            }

            bindingPositionOffset++;

        }
    }
}
