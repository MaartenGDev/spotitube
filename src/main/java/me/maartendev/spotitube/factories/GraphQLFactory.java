package me.maartendev.spotitube.factories;

import graphql.GraphQL;
import graphql.execution.instrumentation.dataloader.DataLoaderDispatcherInstrumentation;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import me.maartendev.spotitube.dao.PlayListDAO;
import me.maartendev.spotitube.dao.TrackDAO;
import me.maartendev.spotitube.dao.UserDAO;
import me.maartendev.spotitube.dto.PlayListDTO;
import me.maartendev.spotitube.dto.TrackDTO;
import me.maartendev.spotitube.dto.requests.LoginResponseDTO;
import me.maartendev.spotitube.graphql.datafetchers.DataFetcherFactory;
import me.maartendev.spotitube.graphql.dataloaders.DataLoaderFactory;
import me.maartendev.spotitube.services.AuthService;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;

import javax.inject.Inject;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;

public class GraphQLFactory {
    private PlayListDAO playListDAO;
    private TrackDAO trackDAO;
    private UserDAO userDAO;
    private AuthService authService;
    private DataFetcherFactory dataFetcherFactory;
    private DataLoaderRegistry dataLoaderRegistry;

    @Inject
    public void setPlayListDAO(PlayListDAO playListDAO) {
        this.playListDAO = playListDAO;
    }

    @Inject
    public void setTrackDAO(TrackDAO trackDAO) {
        this.trackDAO = trackDAO;
    }

    @Inject
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Inject
    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }


    @Inject
    public void setDataFetcherFactory(DataFetcherFactory dataFetcherFactory) {
        this.dataFetcherFactory = dataFetcherFactory;
    }

    public GraphQL build() {
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
        DataFetcher<List<TrackDTO>> tracksDataFetcher = dataFetcherFactory.getTracksDataFetcher(trackDAO);

        // Mutations
        DataFetcher<LoginResponseDTO> loginMutationDataFetcher = dataFetcherFactory.getLoginMutationDataFetcher(authService);
        DataFetcher<List<PlayListDTO>> createPlayListMutationDataFetcher = dataFetcherFactory.getCreatePlayListMutationDataFetcher(playListDAO);
        DataFetcher<List<PlayListDTO>> updatePlayListMutationDataFetcher = dataFetcherFactory.getUpdatePlayListMutationDataFetcher(playListDAO);
        DataFetcher<List<PlayListDTO>> deletePlayListMutationDataFetcher = dataFetcherFactory.getDeletePlayListMutationDataFetcher(playListDAO);
        DataFetcher<List<TrackDTO>> addTrackToPlayListMutationDataFetcher = dataFetcherFactory.getAddTrackToPlayListMutationDataFetcher(trackDAO);
        DataFetcher<List<TrackDTO>> removeTrackFromPlayListMutationDataFetcher = dataFetcherFactory.getRemoveTrackFromPlayListMutationDataFetcher(trackDAO);

        DataLoader<Integer, TrackDTO> trackDataLoader = dataLoaderFactory.getTrackDataLoader(trackDAO);
        registry.register("trackDataLoader", trackDataLoader);
        DataFetcher<CompletableFuture<List<TrackDTO>>> playlistTracksDataFetcher = dataFetcherFactory.getPlayListTrackDataFetcher(trackDataLoader);

        RuntimeWiring runtimeWiring = newRuntimeWiring()
                .type("Query", typeWiring -> typeWiring
                        .dataFetcher("playlists", playListsDataFetcher)
                        .dataFetcher("tracks", tracksDataFetcher)
                )
                .type("PlayList", typeWiring -> typeWiring
                        .dataFetcher("tracks", playlistTracksDataFetcher)
                )
                .type("Mutation", typeWiring -> typeWiring
                        .dataFetcher("login", loginMutationDataFetcher)
                        .dataFetcher("createPlaylist", createPlayListMutationDataFetcher)
                        .dataFetcher("deletePlaylist", deletePlayListMutationDataFetcher)
                        .dataFetcher("updatePlaylist", updatePlayListMutationDataFetcher)
                        .dataFetcher("addTrackToPlaylist", addTrackToPlayListMutationDataFetcher)
                        .dataFetcher("removeTrackFromPlaylist", removeTrackFromPlayListMutationDataFetcher)
                )
                .build();

        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);

        this.dataLoaderRegistry = registry;

        DataLoaderDispatcherInstrumentation dataLoaderDispatcherInstrumentation = new DataLoaderDispatcherInstrumentation();
        return GraphQL.newGraphQL(graphQLSchema)
                .instrumentation(dataLoaderDispatcherInstrumentation)
                .build();
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

    public DataLoaderRegistry getDataLoaderRegistry() {
        return dataLoaderRegistry;
    }
}
