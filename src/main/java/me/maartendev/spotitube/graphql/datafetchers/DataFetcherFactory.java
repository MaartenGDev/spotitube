package me.maartendev.spotitube.graphql.datafetchers;

import graphql.schema.DataFetcher;
import me.maartendev.spotitube.dao.PlayListDAO;
import me.maartendev.spotitube.dto.PlayListDTO;
import me.maartendev.spotitube.dto.TrackDTO;
import org.dataloader.DataLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DataFetcherFactory {

    public DataFetcher<List<PlayListDTO>> getPlayListDataFetcher(PlayListDAO playListDAO) {
        return environment -> new ArrayList<>(playListDAO.all().values());
    }

    public DataFetcher<CompletableFuture<List<TrackDTO>>> getPlayListTrackDataFetcher(DataLoader<Integer, TrackDTO> trackDataLoader) {
        return environment -> {
            PlayListDTO playList = environment.getSource();

            return trackDataLoader.loadMany(playList.getTrackIds());
        };
    }
}
