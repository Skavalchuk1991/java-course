package com.solvd.musicstreamingservice.xml.model;

import jakarta.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "musicService")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlMusicService {

    private String serviceName;
    private int totalStreams;

    @XmlElementWrapper(name = "artists")
    @XmlElement(name = "artist")
    private List<XmlArtist> artists = new ArrayList<>();

    @XmlElementWrapper(name = "users")
    @XmlElement(name = "user")
    private List<XmlUser> users = new ArrayList<>();

    public XmlMusicService() {}

    // Getters and setters
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public int getTotalStreams() { return totalStreams; }
    public void setTotalStreams(int totalStreams) { this.totalStreams = totalStreams; }
    public List<XmlArtist> getArtists() { return artists; }
    public void setArtists(List<XmlArtist> artists) { this.artists = artists; }
    public List<XmlUser> getUsers() { return users; }
    public void setUsers(List<XmlUser> users) { this.users = users; }

    @Override
    public String toString() {
        return "XmlMusicService{name='" + serviceName + "', artists=" + artists.size() + ", users=" + users.size() + "}";
    }
}
