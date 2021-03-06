package me.maartendev.spotitube.dao;

import me.maartendev.spotitube.dto.PlayListDTO;
import me.maartendev.spotitube.dto.TrackDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TrackDAOTest extends DatabaseTest {
    private TrackDAO trackDAO;
    private PlayListDAO playListDAO;

    private TrackDAO getTrackDAO() {
        if (trackDAO != null) {
            return trackDAO;
        }

        TrackDAO trackDAO = new TrackDAO();
        trackDAO.setDataSource(this.getDataSource());

        this.trackDAO = trackDAO;
        return trackDAO;
    }

    private PlayListDAO getPlayListDAO() {
        if (playListDAO != null) {
            return playListDAO;
        }

        PlayListDAO playListDAO = new PlayListDAO();
        playListDAO.setDataSource(this.getDataSource());
        playListDAO.setTrackDAO(this.getTrackDAO());

        this.playListDAO = playListDAO;
        return playListDAO;
    }

    @Test
    public void testCreateStoresTrackInDatabase() {
        TrackDAO trackDAO = this.getTrackDAO();
        TrackDTO createdTrack = trackDAO.create(this.createTestTrackDTO());

        Assertions.assertEquals(createdTrack, trackDAO.find(createdTrack.getId()));
    }

    @Test
    public void testTrackIsAssociatedWithPlayListAfterAssociation() {
        TrackDAO trackDAO = this.getTrackDAO();
        PlayListDAO playListDAO = this.getPlayListDAO();

        PlayListDTO createdPlayList = playListDAO.create(1, new PlayListDTO(-1, "Playlists 1", false, new ArrayList<>()));
        TrackDTO createdTrack = trackDAO.create(this.createTestTrackDTO());
        trackDAO.associateWithPlayList(createdPlayList.getId(), createdTrack);

        Assertions.assertTrue(trackDAO.allForPlaylistId(createdPlayList.getId()).getTracks().contains(createdTrack));
    }

    @Test
    public void testAssociateReturnsFalseIfTheAssociationFailed() {
        TrackDAO trackDAO = this.getTrackDAO();
        PlayListDAO playListDAO = this.getPlayListDAO();

        PlayListDTO createdPlayList = playListDAO.create(1, new PlayListDTO(-1, "Playlists 1", false, new ArrayList<>()));
        TrackDTO createdTrack = trackDAO.create(this.createTestTrackDTO());
        boolean successfullyAssociatedTrack = trackDAO.associateWithPlayList(-1, createdTrack);

        Assertions.assertFalse(successfullyAssociatedTrack);

        Assertions.assertFalse(trackDAO.allForPlaylistId(createdPlayList.getId()).getTracks().contains(createdTrack));
    }

    @Test
    public void testTrackIsNotAssociatedWithPlayListAfterDisassociation() {
        TrackDAO trackDAO = this.getTrackDAO();
        PlayListDAO playListDAO = this.getPlayListDAO();

        PlayListDTO createdPlayList = playListDAO.create(1, new PlayListDTO(-1, "Playlists 1", false, new ArrayList<>()));
        TrackDTO createdTrack = trackDAO.create(this.createTestTrackDTO());
        trackDAO.associateWithPlayList(createdPlayList.getId(), createdTrack);

        Assertions.assertTrue(trackDAO.allForPlaylistId(createdPlayList.getId()).getTracks().contains(createdTrack));

        trackDAO.disassociateWithPlayList(createdPlayList.getId(), createdTrack.getId());

        Assertions.assertFalse(trackDAO.allForPlaylistId(createdPlayList.getId()).getTracks().contains(createdTrack));
    }

    @Test
    public void testAssociateReturnsFalseIfTheDisassociationFailed() {
        TrackDAO trackDAO = this.getTrackDAO();
        PlayListDAO playListDAO = this.getPlayListDAO();

        PlayListDTO createdPlayList = playListDAO.create(1, new PlayListDTO(-1, "Playlists 1", false, new ArrayList<>()));
        TrackDTO createdTrack = trackDAO.create(this.createTestTrackDTO());
        trackDAO.associateWithPlayList(1, createdTrack);
        trackDAO.disassociateWithPlayList(-1, -1);

        Assertions.assertTrue(trackDAO.allForPlaylistId(createdPlayList.getId()).getTracks().contains(createdTrack));
    }

    @Test
    public void testAllReturnsAllCreatedTracks() {
        TrackDAO trackDAO = this.getTrackDAO();

        TrackDTO trackToDuplicate = this.createTestTrackDTO();
        List<TrackDTO> tracksToCreate = new ArrayList<>();
        tracksToCreate.add(trackToDuplicate);
        tracksToCreate.add(trackToDuplicate);
        tracksToCreate.add(trackToDuplicate);

        for (TrackDTO trackToCreate : tracksToCreate) {
            trackDAO.create(trackToCreate);
        }


        Assertions.assertEquals(tracksToCreate.size(), trackDAO.all().getTracks().size());
    }

    @Test
    public void testAllNotInPlayListIdShouldReturnAllTracksThatAreNotInThePlayList() {
        TrackDAO trackDAO = this.getTrackDAO();
        PlayListDAO playListDAO = this.getPlayListDAO();

        PlayListDTO firstPlayList = playListDAO.create(1, new PlayListDTO(-1, "Playlists 1", false, new ArrayList<>()));
        PlayListDTO secondPlayList = playListDAO.create(1, new PlayListDTO(-1, "Playlists 2", false, new ArrayList<>()));

        // Associate Track with the first playList
        TrackDTO createdTrack = trackDAO.create(this.createTestTrackDTO());
        trackDAO.associateWithPlayList(firstPlayList.getId(), createdTrack);

        Assertions.assertEquals(0, trackDAO.allNotInPlaylistId(firstPlayList.getId()).getTracks().size());
        Assertions.assertEquals(1, trackDAO.allNotInPlaylistId(secondPlayList.getId()).getTracks().size());
    }

    private TrackDTO createTestTrackDTO() {
        TrackDTO trackDTO = new TrackDTO();

        trackDTO.setId(1);
        trackDTO.setTitle("Hello");
        trackDTO.setPerformer("World");
        trackDTO.setDuration(20);
        trackDTO.setAlbum("Latest");
        trackDTO.setPublicationDate(new Date());
        trackDTO.setDescription("Nice Song");
        trackDTO.setOfflineAvailable(false);

        return trackDTO;
    }
}
