package me.maartendev.spotitube;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.execution.instrumentation.dataloader.DataLoaderDispatcherInstrumentation;


import graphql.schema.DataFetcher;
import me.maartendev.spotitube.dao.PlayListDAO;
import me.maartendev.spotitube.dao.TrackDAO;
import me.maartendev.spotitube.dto.PlayListDTO;
import me.maartendev.spotitube.dto.TrackDTO;
import me.maartendev.spotitube.dto.requests.GraphQLRequest;
import me.maartendev.spotitube.dto.requests.GraphQLResponse;
import me.maartendev.spotitube.graphql.datafetchers.DataFetcherFactory;
import me.maartendev.spotitube.graphql.dataloaders.DataLoaderFactory;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;

@Path("/graphql")
public class GraphQLController {
    private static final Logger LOGGER = Logger.getLogger(GraphQLController.class.getName());

    private PlayListDAO playListDAO;
    private TrackDAO trackDAO;
    private DataFetcherFactory dataFetcherFactory;

    @Inject
    public void setPlayListDAO(PlayListDAO playListDAO) {
        this.playListDAO = playListDAO;
    }

    @Inject
    public void setTrackDAO(TrackDAO trackDAO) {
        this.trackDAO = trackDAO;
    }

    @Inject
    public void setDataFetcherFactory(DataFetcherFactory dataFetcherFactory) {
        this.dataFetcherFactory = dataFetcherFactory;
    }

    @GET
    @Produces(MediaType.TEXT_HTML + "; charset=UTF-8")
    public Response ide() throws IOException {
        String fileContent = new String(Files.readAllBytes(Paths.get(this.getResourcePath("index.html"))), StandardCharsets.UTF_8);

        return Response.ok(fileContent).build();

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response runQuery(GraphQLRequest request) {
        SchemaParser schemaParser = new SchemaParser();
        File querySchema = this.getResourceFile("query.graphqls");
        File mutationSchema = this.getResourceFile("mutation.graphqls");
        TypeDefinitionRegistry typeRegistryQuery = schemaParser.parse(querySchema);
        TypeDefinitionRegistry typeRegistryMutation = schemaParser.parse(mutationSchema);
        TypeDefinitionRegistry typeRegistry = new TypeDefinitionRegistry();
        typeRegistry.merge(typeRegistryQuery);
        typeRegistry.merge(typeRegistryMutation);

        DataLoaderRegistry registry = new DataLoaderRegistry();
        DataLoaderFactory dataLoaderFactory = new DataLoaderFactory();

        DataFetcher<List<PlayListDTO>> playListsDataFetcher = dataFetcherFactory.getPlayListDataFetcher(playListDAO);

        DataLoader<Integer, TrackDTO> trackDataLoader = dataLoaderFactory.getTrackDataLoader(trackDAO);
        registry.register("trackDataLoader", trackDataLoader);
        DataFetcher<CompletableFuture<List<TrackDTO>>> playlistTracksDataFetcher = dataFetcherFactory.getPlayListTrackDataFetcher(trackDataLoader);

        RuntimeWiring runtimeWiring = newRuntimeWiring()
                .type("Query", typeWiring -> typeWiring
                        .dataFetcher("playLists", playListsDataFetcher)
                )
                .type("PlayList", typeWiring -> typeWiring
                        .dataFetcher("tracks", playlistTracksDataFetcher)
                )
                .build();

        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);

        ExecutionInput executionInput = ExecutionInput.newExecutionInput()
                .query(request.getQuery())
                .variables(request.getVariables())
                .build();

        DataLoaderDispatcherInstrumentation dataLoaderDispatcherInstrumentation = new DataLoaderDispatcherInstrumentation(registry);
        GraphQL build = GraphQL.newGraphQL(graphQLSchema)
                .instrumentation(dataLoaderDispatcherInstrumentation)
                .build();

        ExecutionResult executionResult = build.execute(executionInput);

        return Response.ok(new GraphQLResponse(executionResult.getData())).build();
    }

    private File getResourceFile(String filename) {
        return new File(this.getResourcePath(filename));
    }

    private String getResourcePath(String filename) {
        URL schemaURL = getClass().getClassLoader().getResource(filename);

        if (schemaURL == null) {
            throw new RuntimeException("GraphQL schema not found in resources");
        }

        return schemaURL.getFile();
    }
}
