package me.maartendev.spotitube.controllers;

import me.maartendev.spotitube.PlayListTrackController;
import me.maartendev.spotitube.dao.TrackDAO;
import me.maartendev.spotitube.dto.TrackCollectionDTO;
import me.maartendev.spotitube.dto.TrackDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class PlayListTrackControllerTest {
    @Test
    public void testShouldReturnNotFoundIfNoPlayListWasFound() {
        PlayListTrackController playListTrackController = new PlayListTrackController();

        TrackDAO trackDAO = Mockito.mock(TrackDAO.class);
        playListTrackController.setTrackDAO(trackDAO);
        Mockito.when(trackDAO.allForPlaylistId(Mockito.anyInt())).thenReturn(null);

        Response response = playListTrackController.show(-1);

        Assertions.assertEquals(404, response.getStatus());
    }

    @Test
    public void testShouldReturnOkIfThePlayListWereFound() {
        PlayListTrackController playListTrackController = new PlayListTrackController();

        TrackDAO trackDAO = Mockito.mock(TrackDAO.class);
        playListTrackController.setTrackDAO(trackDAO);

        List<TrackDTO> tracks = new ArrayList<>();
        tracks.add(new TrackDTO());

        TrackCollectionDTO trackCollectionToReturn = new TrackCollectionDTO(tracks);

        Mockito.when(trackDAO.allForPlaylistId(Mockito.anyInt())).thenReturn(trackCollectionToReturn);

        Response response = playListTrackController.show(1);

        Assertions.assertEquals(200, response.getStatus());
    }
}
