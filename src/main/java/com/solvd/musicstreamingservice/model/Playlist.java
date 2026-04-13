package com.solvd.musicstreamingservice.model;

import com.solvd.musicstreamingservice.exception.PlaylistFullException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Represents a playlist that contains media items
 */
public class Playlist implements Shareable {

    private static final Logger LOGGER = LogManager.getLogger(Playlist.class);

    // Name of playlist
    private String name;

    // Playlist can contain different types of Media (Song, Podcast)
    private List<Media> items;

    /**
     * Constructor
     */
    public Playlist(String name, List<Media> items) {
        this.name = name;
        this.items = items;
    }

    public void addItem(Media media) {
        if (items.size() >= AppConstants.MAX_PLAYLIST_SIZE) {
            throw new PlaylistFullException("Playlist '" + name + "' is full. Max size: " + AppConstants.MAX_PLAYLIST_SIZE);
        }
        items.add(media);
    }

    // -------- Shareable --------

    @Override
    public String getShareLink() {
        return "https://music.app/playlists/" + name.toLowerCase().replace(" ", "-");
    }

    @Override
    public void share(User recipient) {
        LOGGER.info("Playlist '{}' shared with {}", name, recipient.getUsername());
    }

    /**
     * Business method:
     * Calculates total duration of all media in playlist
     */
    public int calculateTotalDuration() {
        return items.stream()
                .mapToInt(Media::getDuration)
                .sum();
    }

    // -------- Getters --------

    public String getName() {
        return name;
    }

    public List<Media> getItems() {
        return items;
    }

    // -------- Setters --------

    public void setName(String name) {
        this.name = name;
    }

    public void setItems(List<Media> items) {
        this.items = items;
    }
}
