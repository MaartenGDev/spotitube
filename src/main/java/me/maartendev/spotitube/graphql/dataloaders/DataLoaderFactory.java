package me.maartendev.spotitube.graphql.dataloaders;

import me.maartendev.spotitube.dao.TrackDAO;
import me.maartendev.spotitube.dto.TrackDTO;
import org.dataloader.BatchLoader;
import org.dataloader.DataLoader;

import java.util.concurrent.CompletableFuture;

public class DataLoaderFactory {

    public DataLoader<Integer, TrackDTO> getTrackDataLoader(TrackDAO trackDAO) {
        BatchLoader<Integer, TrackDTO> trackBatchLoader = keys -> CompletableFuture.supplyAsync(() -> trackDAO.read(keys));
        return new DataLoader<>(trackBatchLoader);
    }
}
