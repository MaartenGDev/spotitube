package me.maartendev.spotitube.graphql.exceptions;

import graphql.GraphQLException;

public class AuthorizationRequiredException extends GraphQLException {
    public AuthorizationRequiredException(String message){
        super(message);
    }
}
