package com.solvd.musicstreamingservice.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents user's personal media library.
 * Independent domain entity.
 */
public class Library {

    // Owner of the library
    private User owner;

    // Stored media in library
    private List<Media> mediaItems;

    /**
     * Constructor initializes library with empty media list
     */
    public Library(User owner) {
        this.owner = owner;
        this.mediaItems = new ArrayList<>();
    }

    // -------- Getters --------

    public User getOwner() {
        return owner;
    }

    public List<Media> getMediaItems() {
        return mediaItems;
    }

    // -------- Business Methods --------

    /**
     * Adds media to library.
     */
    public void addMedia(Media media) {
        mediaItems.add(media);
    }

    /**
     * Prints library content
     */
    public void printLibrary() {

        System.out.println("Library of user: " + owner.getUsername());

        for (Media media : mediaItems) {
            System.out.println("- " + media.getTitle());
        }
    }

    // ---------- Setters ----------

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setMediaItems(List<Media> mediaItems) {
        this.mediaItems = mediaItems;
    }
}
