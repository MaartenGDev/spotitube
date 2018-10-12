package me.maartendev.spotitube;

import me.maartendev.spotitube.dao.TrackDAO;
import me.maartendev.spotitube.dto.TrackCollectionDTO;
import me.maartendev.spotitube.dto.TrackDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class TrackControllerTest {
    @Test
    public void testShouldReturnAllTracksIfNoPlayListIdWasSpecified() {
        TrackController trackController = new TrackController();

        TrackDAO trackDAO = Mockito.mock(TrackDAO.class);
        trackController.setTrackDAO(trackDAO);

        List<TrackDTO> tracks = new ArrayList<>();
        tracks.add(new TrackDTO());

        TrackCollectionDTO trackCollectionToReturn = new TrackCollectionDTO(tracks);

        Mockito.when(trackDAO.all()).thenReturn(trackCollectionToReturn);

        TrackCollectionDTO returnedCollection = (TrackCollectionDTO) trackController.index(0).getEntity();

        Assertions.assertEquals(returnedCollection.getTracks().size(), trackCollectionToReturn.getTracks().size());
    }


    @Test
    public void testShouldReturnAllTracksOfAPlayListIfAnIdWasSpecified() {
        TrackController trackController = new TrackController();

        TrackDAO trackDAO = Mockito.mock(TrackDAO.class);
        trackController.setTrackDAO(trackDAO);

        List<TrackDTO> tracks = new ArrayList<>();
        tracks.add(new TrackDTO());

        TrackCollectionDTO trackCollectionToReturn = new TrackCollectionDTO(tracks);

        Mockito.when(trackDAO.allNotInPlaylistId(3)).thenReturn(trackCollectionToReturn);

        TrackCollectionDTO returnedCollection = (TrackCollectionDTO) trackController.index(3).getEntity();

        Assertions.assertEquals(returnedCollection.getTracks().size(), trackCollectionToReturn.getTracks().size());
    }
}
