package me.maartendev.spotitube;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import me.maartendev.spotitube.dao.UserDAO;
import me.maartendev.spotitube.dto.UserDTO;
import me.maartendev.spotitube.dto.requests.GraphQLRequest;
import me.maartendev.spotitube.dto.requests.GraphQLResponse;
import me.maartendev.spotitube.graphql.RequestContext;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Path("/graphql")
public class GraphQLController {
    private GraphQL graphQL;
    private UserDAO userDAO;

    @Inject
    public void setGraphQL(GraphQL graphQL) {
        this.graphQL = graphQL;
    }

    @Inject
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GET
    @Produces(MediaType.TEXT_HTML + "; charset=UTF-8")
    public Response ide() throws IOException {
        String fileContent = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("index.html").getFile())), StandardCharsets.UTF_8);

        return Response.ok(fileContent).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response runQuery(@HeaderParam("Authorization") String bearerToken, GraphQLRequest request) {
        String token = getTokenFromHeader(bearerToken);
        UserDTO authenticatedUser = userDAO.findByToken(token);

        RequestContext context = new RequestContext();
        context.setUser(authenticatedUser);

        ExecutionInput executionInput = ExecutionInput.newExecutionInput()
                .query(request.getQuery())
                .variables(request.getVariables())
                .context(context)
                .build();


        ExecutionResult executionResult = graphQL.execute(executionInput);

        return Response.ok(new GraphQLResponse(executionResult.getData(), executionResult.getErrors())).build();
    }


    private String getTokenFromHeader(String rawHeaderValue){
        return rawHeaderValue.replaceFirst("Bearer ", "");
    }
}