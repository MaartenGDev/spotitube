package me.maartendev.javaee.dto;

import java.util.List;

public class TrackCollectionDTO {
    private List<TrackDTO> tracks;

    public List<TrackDTO> getTracks() {
        return tracks;
    }

    public TrackCollectionDTO(List<TrackDTO> tracks) {
        this.tracks = tracks;
    }
}
