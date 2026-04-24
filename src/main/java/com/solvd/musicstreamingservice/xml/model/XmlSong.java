package com.solvd.musicstreamingservice.xml.model;

import jakarta.xml.bind.annotation.*;
import java.time.LocalDateTime;

@XmlAccessorType(XmlAccessType.FIELD)
public class XmlSong {

    @XmlAttribute
    private int id;

    private String title;
    private String artistName;
    private String genreName;
    private int duration;
    private boolean availableOffline;
    private LocalDateTime addedAt;

    public XmlSong() {}

    public XmlSong(int id, String title, String artistName, String genreName, int duration, boolean availableOffline, LocalDateTime addedAt) {
        this.id = id;
        this.title = title;
        this.artistName = artistName;
        this.genreName = genreName;
        this.duration = duration;
        this.availableOffline = availableOffline;
        this.addedAt = addedAt;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getArtistName() { return artistName; }
    public void setArtistName(String artistName) { this.artistName = artistName; }
    public String getGenreName() { return genreName; }
    public void setGenreName(String genreName) { this.genreName = genreName; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    public boolean isAvailableOffline() { return availableOffline; }
    public void setAvailableOffline(boolean availableOffline) { this.availableOffline = availableOffline; }
    public LocalDateTime getAddedAt() { return addedAt; }
    public void setAddedAt(LocalDateTime addedAt) { this.addedAt = addedAt; }

    @Override
    public String toString() {
        return "XmlSong{id=" + id + ", title='" + title + "', artist='" + artistName + "', duration=" + duration + "}";
    }
}
