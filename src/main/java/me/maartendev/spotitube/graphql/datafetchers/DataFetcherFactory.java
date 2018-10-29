package me.maartendev.spotitube.graphql.datafetchers;

import graphql.schema.DataFetcher;
import me.maartendev.spotitube.dao.PlayListDAO;
import me.maartendev.spotitube.dao.TrackDAO;
import me.maartendev.spotitube.dto.PlayListDTO;
import me.maartendev.spotitube.dto.TrackDTO;
import me.maartendev.spotitube.dto.UserDTO;
import me.maartendev.spotitube.dto.requests.LoginResponseDTO;
import me.maartendev.spotitube.graphql.RequestContext;
import me.maartendev.spotitube.graphql.exceptions.AuthorizationRequiredException;
import me.maartendev.spotitube.graphql.exceptions.UnauthorizedException;
import me.maartendev.spotitube.services.AuthService;
import org.dataloader.DataLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class DataFetcherFactory {
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

    public DataFetcher<List<PlayListDTO>> getCreatePlayListMutationDataFetcher(PlayListDAO playListDAO) {
        return environment -> {
            RequestContext context = environment.getContext();
            UserDTO authenticatedUser = context.getUser();

            if(authenticatedUser == null){
                throw new AuthorizationRequiredException("Authorization Required");
            }

            Map<String, Object> inputValues = environment.getArgument("input");

            PlayListDTO playListDTO = new PlayListDTO();
            playListDTO.setName((String) inputValues.get("name"));

            playListDAO.create(authenticatedUser.getId(), playListDTO);

            return new ArrayList<>(playListDAO.allTailoredToUserIdWithTrackIds(authenticatedUser.getId()).values());
        };
    }


    public DataFetcher<List<PlayListDTO>> getUpdatePlayListMutationDataFetcher(PlayListDAO playListDAO) {
        return environment -> {
            RequestContext context = environment.getContext();
            UserDTO authenticatedUser = context.getUser();

            if(authenticatedUser == null){
                throw new AuthorizationRequiredException("Authorization Required");
            }

            Map<String, Object> inputValues = environment.getArgument("input");
            PlayListDTO playListDTO = new PlayListDTO();
            playListDTO.setName((String) inputValues.get("name"));

            int playListId = environment.getArgument("id");

            playListDAO.update(playListId, playListDTO);

            return new ArrayList<>(playListDAO.allTailoredToUserIdWithTrackIds(authenticatedUser.getId()).values());
        };
    }


    public DataFetcher<List<PlayListDTO>> getDeletePlayListMutationDataFetcher(PlayListDAO playListDAO) {
        return environment -> {
            RequestContext context = environment.getContext();
            UserDTO authenticatedUser = context.getUser();

            if (authenticatedUser == null) {
                throw new AuthorizationRequiredException("Authorization Required");
            }

            int playListId = environment.getArgument("id");
            playListDAO.delete(playListId);

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

    public DataFetcher<List<TrackDTO>> getTracksDataFetcher(TrackDAO trackDAO) {
        return environment -> {
            Map<String, Object> arguments = environment.getArguments();

            if (arguments.containsKey("notInPlaylistId") && arguments.get("notInPlaylistId") != null) {
                int playListId = environment.getArgument("notInPlaylistId");
                return trackDAO.allNotInPlaylistId(playListId).getTracks();
            }

            if (arguments.containsKey("inPlaylistId") && arguments.get("inPlaylistId") != null) {
                int playListId = environment.getArgument("inPlaylistId");
                return trackDAO.allForPlaylistId(playListId).getTracks();
            }

            return trackDAO.all().getTracks();
        };
    }

    public DataFetcher<List<TrackDTO>> getRemoveTrackFromPlayListMutationDataFetcher(TrackDAO trackDAO) {
        return environment -> {
            int playListId = environment.getArgument("playlistId");
            int trackId = environment.getArgument("trackId");

            trackDAO.disassociateWithPlayList(playListId, trackId);

            return trackDAO.allForPlaylistId(playListId).getTracks();
        };
    }

    public DataFetcher<List<TrackDTO>> getAddTrackToPlayListMutationDataFetcher(TrackDAO trackDAO) {
        return environment -> {
            int playListId = environment.getArgument("playlistId");
            int trackId = environment.getArgument("trackId");
            boolean offlineAvailable = environment.getArgument("offlineAvailable");

            TrackDTO trackConnectionDetails = new TrackDTO();
            trackConnectionDetails.setId(trackId);
            trackConnectionDetails.setOfflineAvailable(offlineAvailable);

            trackDAO.associateWithPlayList(playListId, trackConnectionDetails);

            return trackDAO.allForPlaylistId(playListId).getTracks();
        };
    }

}
