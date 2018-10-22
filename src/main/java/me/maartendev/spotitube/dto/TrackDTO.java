package me.maartendev.spotitube.dto;


import java.text.SimpleDateFormat;
import java.util.Date;

public class TrackDTO {
    private int id;
    private String title;
    private String performer;
    private int duration;
    private String album;
    private int playcount;
    private Date publicationDate;
    private String description;
    private boolean offlineAvailable;

    public TrackDTO setId(int id) {
        this.id = id;
        return this;
    }

    public TrackDTO setTitle(String title) {
        this.title = title;
        return this;
    }

    public TrackDTO setPerformer(String performer) {
        this.performer = performer;
        return this;
    }

    public TrackDTO setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public TrackDTO setAlbum(String album) {
        this.album = album;
        return this;
    }

    public TrackDTO setPlaycount(int playcount) {
        this.playcount = playcount;
        return this;
    }

    public TrackDTO setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
        return this;
    }

    public TrackDTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public TrackDTO setOfflineAvailable(boolean offlineAvailable) {
        this.offlineAvailable = offlineAvailable;
        return this;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPerformer() {
        return performer;
    }

    public int getDuration() {
        return duration;
    }


    public String getAlbum() {
        return album;
    }

    public int getPlaycount() {
        return playcount;
    }

    public String getPublicationDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(publicationDate);
    }

    public String getDescription() {
        return description;
    }


    public boolean isOfflineAvailable() {
        return offlineAvailable;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if(!(object instanceof TrackDTO)) return false;
        TrackDTO trackDTO =(TrackDTO) object;

        return this.getId() == trackDTO.getId()
                && this.getTitle().equals(trackDTO.getTitle())
                && this.getPerformer().equals(trackDTO.getPerformer())
                && this.getDuration() == trackDTO.getDuration()
                && this.getAlbum().equals(trackDTO.getAlbum())
                && this.getPlaycount() == trackDTO.getPlaycount()
                && this.getPublicationDate().equals(trackDTO.getPublicationDate())
                && this.getDescription().equals(trackDTO.getDescription())
                && this.isOfflineAvailable() == trackDTO.isOfflineAvailable();

    }
}
