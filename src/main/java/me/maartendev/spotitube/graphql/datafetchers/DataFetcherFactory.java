package me.maartendev.spotitube.graphql.datafetchers;

import graphql.schema.DataFetcher;
import me.maartendev.spotitube.dao.PlayListDAO;
import me.maartendev.spotitube.dao.UserDAO;
import me.maartendev.spotitube.dto.PlayListDTO;
import me.maartendev.spotitube.dto.TrackDTO;
import me.maartendev.spotitube.dto.UserDTO;
import me.maartendev.spotitube.dto.requests.LoginResponseDTO;
import me.maartendev.spotitube.graphql.RequestContext;
import me.maartendev.spotitube.graphql.exceptions.AuthorizationRequiredException;
import me.maartendev.spotitube.graphql.exceptions.UnauthorizedException;
import me.maartendev.spotitube.services.AuthService;
import org.dataloader.DataLoader;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class DataFetcherFactory {
    public DataFetcher<PlayListDTO> getCreatePlayListMutationDataFetcher(PlayListDAO playListDAO) {
        return environment -> {
            RequestContext context = environment.getContext();
            UserDTO authenticatedUser = context.getUser();

            Map<String, Object> inputValues = environment.getArgument("input");

            PlayListDTO playListDTO = new PlayListDTO();
            playListDTO.setName((String) inputValues.get("name"));

            return playListDAO.create(authenticatedUser.getId(), playListDTO);
        };
    }

    public DataFetcher<List<PlayListDTO>> getPlayListDataFetcher(PlayListDAO playListDAO) {
        return environment -> {
            RequestContext context = environment.getContext();
            UserDTO authenticatedUser = context.getUser();

            if (authenticatedUser == null) {
                throw new AuthorizationRequiredException("Authorization Required");
            }

            return new ArrayList<>(playListDAO.allTailoredToUserIdWithTrackIds(authenticatedUser.getId()).values());
        };
    }

    public DataFetcher<CompletableFuture<List<TrackDTO>>> getPlayListTrackDataFetcher(DataLoader<Integer, TrackDTO> trackDataLoader) {
        return environment -> {
            PlayListDTO playList = environment.getSource();

            return trackDataLoader.loadMany(playList.getTrackIds());
        };
    }

    public DataFetcher<LoginResponseDTO> getLoginMutationDataFetcher(AuthService authService) {
        return environment -> {
            Map<String, Object> inputValues = environment.getArgument("input");
            String user = (String) inputValues.get("user");
            String password = (String) inputValues.get("password");

            if (!authService.isValid(user, password)) {
                throw new UnauthorizedException("Login Failed");
            }

            LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
            loginResponseDTO.setUser(user);
            loginResponseDTO.setToken(authService.getTokenForUsername(user));

            return loginResponseDTO;
        };
    }
}
