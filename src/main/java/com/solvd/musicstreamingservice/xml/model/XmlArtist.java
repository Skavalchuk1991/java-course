package com.solvd.musicstreamingservice.xml.model;

import jakarta.xml.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class XmlArtist {

    private String name;
    private String country;
    private int debutYear;
    private boolean verified;
    private LocalDateTime lastUpdated;

    @XmlElementWrapper(name = "albums")
    @XmlElement(name = "album")
    private List<XmlAlbum> albums = new ArrayList<>();

    public XmlArtist() {}

    public XmlArtist(String name, String country, int debutYear, boolean verified, LocalDateTime lastUpdated) {
        this.name = name;
        this.country = country;
        this.debutYear = debutYear;
        this.verified = verified;
        this.lastUpdated = lastUpdated;
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public int getDebutYear() { return debutYear; }
    public void setDebutYear(int debutYear) { this.debutYear = debutYear; }
    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
    public List<XmlAlbum> getAlbums() { return albums; }
    public void setAlbums(List<XmlAlbum> albums) { this.albums = albums; }

    @Override
    public String toString() {
        return "XmlArtist{name='" + name + "', country='" + country + "', albums=" + albums.size() + "}";
    }
}
