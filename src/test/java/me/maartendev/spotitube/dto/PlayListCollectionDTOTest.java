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
        tracks.add(createTestTrackDTO().setDuration(20));
        tracks.add(createTestTrackDTO().setDuration(40));

        playLists.add(new PlayListDTO(1, "PlayList1", false, tracks));
        PlayListCollectionDTO playListCollectionDTO = new PlayListCollectionDTO(playLists);

        Assertions.assertEquals(60, playListCollectionDTO.getLength());
    }

    private TrackDTO createTestTrackDTO() {
        return new TrackDTO()
                .setId(1)
                .setTitle("Hello")
                .setPerformer("World")
                .setDuration(20)
                .setAlbum("Latest")
                .setDuration(10)
                .setPublicationDate(new Date())
                .setDescription("Nice Song")
                .setOfflineAvailable(false);
    }
}
