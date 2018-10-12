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

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPerformer(String performer) {
        this.performer = performer;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setPlaycount(int playcount) {
        this.playcount = playcount;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOfflineAvailable(boolean offlineAvailable) {
        this.offlineAvailable = offlineAvailable;
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

    public TrackDTO(){

    }

    public TrackDTO(int id, String title, String performer, int duration, String album, int playcount, Date publicationDate, String description, boolean offlineAvailable) {
        this.id = id;
        this.title = title;
        this.performer = performer;
        this.duration = duration;
        this.album = album;
        this.playcount = playcount;
        this.publicationDate = publicationDate;
        this.description = description;
        this.offlineAvailable = offlineAvailable;
    }
}
