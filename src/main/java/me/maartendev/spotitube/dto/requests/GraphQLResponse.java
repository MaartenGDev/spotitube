package me.maartendev.spotitube.dto.requests;

public class GraphQLResponse {
    private Object data;

    public GraphQLResponse(Object data){
        this.data = data;
    }

    public Object getData() {
        return data;
    }
}
