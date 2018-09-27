package me.maartendev.javaee.dto;

import java.util.List;

public class TracksDTO {
    public List<TrackDTO> getTracks() {
        return tracks;
    }

    private List<TrackDTO> tracks;

    public TracksDTO(List<TrackDTO> tracks) {
        this.tracks = tracks;
    }
}
