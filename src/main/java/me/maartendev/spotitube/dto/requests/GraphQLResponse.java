package me.maartendev.spotitube.dto.requests;

import graphql.GraphQLError;

import java.util.List;

public class GraphQLResponse {
    private Object data;
    private List<GraphQLError> errors;

    public GraphQLResponse(Object data, List<GraphQLError> errors){
        this.data = data;
        this.errors = errors;
    }

    public Object getData() {
        return data;
    }

    public Object getErrors() {
        return errors;
    }
}
