package com.solvd.musicstreamingservice.xml.model;

import jakarta.xml.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class XmlAlbum {

    private String title;
    private String releaseDate;
    private int totalTracks;
    private boolean explicit;
    private LocalDateTime createdAt;

    @XmlElementWrapper(name = "songs")
    @XmlElement(name = "song")
    private List<XmlSong> songs = new ArrayList<>();

    public XmlAlbum() {}

    public XmlAlbum(String title, String releaseDate, int totalTracks, boolean explicit, LocalDateTime createdAt) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.totalTracks = totalTracks;
        this.explicit = explicit;
        this.createdAt = createdAt;
    }

    // Getters and setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }
    public int getTotalTracks() { return totalTracks; }
    public void setTotalTracks(int totalTracks) { this.totalTracks = totalTracks; }
    public boolean isExplicit() { return explicit; }
    public void setExplicit(boolean explicit) { this.explicit = explicit; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public List<XmlSong> getSongs() { return songs; }
    public void setSongs(List<XmlSong> songs) { this.songs = songs; }

    @Override
    public String toString() {
        return "XmlAlbum{title='" + title + "', tracks=" + totalTracks + ", songs=" + songs.size() + "}";
    }
}
