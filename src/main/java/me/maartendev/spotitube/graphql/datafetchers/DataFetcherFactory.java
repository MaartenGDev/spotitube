package me.maartendev.spotitube.graphql.datafetchers;

import graphql.schema.DataFetcher;
import me.maartendev.spotitube.dao.PlayListDAO;
import me.maartendev.spotitube.dto.PlayListDTO;
import me.maartendev.spotitube.dto.TrackDTO;
import me.maartendev.spotitube.dto.UserDTO;
import me.maartendev.spotitube.graphql.RequestContext;
import me.maartendev.spotitube.graphql.exceptions.AuthorizationRequiredException;
import org.dataloader.DataLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DataFetcherFactory {

    public DataFetcher<List<PlayListDTO>> getPlayListDataFetcher(PlayListDAO playListDAO) {
        return environment -> {
            RequestContext context = environment.getContext();
            UserDTO authenticatedUser = context.getUser();

            if(authenticatedUser == null){
                throw new AuthorizationRequiredException("Authorization Required");
            }

            return  new ArrayList<>(playListDAO.allTailoredToUserIdWithTrackIds(authenticatedUser.getId()).values());
        };
    }

    public DataFetcher<CompletableFuture<List<TrackDTO>>> getPlayListTrackDataFetcher(DataLoader<Integer, TrackDTO> trackDataLoader) {
        return environment -> {
            PlayListDTO playList = environment.getSource();

            return trackDataLoader.loadMany(playList.getTrackIds());
        };
    }
}
