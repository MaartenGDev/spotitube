package me.maartendev.spotitube.graphql.exceptions;

import graphql.GraphQLException;

public class UnauthorizedException extends GraphQLException {
    public UnauthorizedException(String message){
        super(message);
    }
}
