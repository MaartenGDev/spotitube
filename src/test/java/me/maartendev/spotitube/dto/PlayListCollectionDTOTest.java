package me.maartendev.spotitube.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlayListCollectionDTOTest {
    @Test
    public void shouldReturnTheTotalForAllTracks() {
        List<PlayListDTO> playLists = new ArrayList<>();
        List<TrackDTO> tracks = new ArrayList<>();
        tracks.add(createTestTrackDTOWithDuration(20));
        tracks.add(createTestTrackDTOWithDuration(40));

        playLists.add(new PlayListDTO(1, "PlayList1", false, tracks));
        PlayListCollectionDTO playListCollectionDTO = new PlayListCollectionDTO(playLists);

        Assertions.assertEquals(60, playListCollectionDTO.getLength());
    }

    private TrackDTO createTestTrackDTOWithDuration(int duration) {
        TrackDTO trackDTO = new TrackDTO();
        trackDTO.setId(1);
        trackDTO.setTitle("Hello");
        trackDTO.setPerformer("World");
        trackDTO.setDuration(duration);
        trackDTO.setAlbum("Latest");
        trackDTO.setPublicationDate(new Date());
        trackDTO.setDescription("Nice Song");
        trackDTO.setOfflineAvailable(false);

        return trackDTO;
    }
}
