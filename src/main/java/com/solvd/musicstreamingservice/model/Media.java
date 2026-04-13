package com.solvd.musicstreamingservice.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Abstract base class that represents any playable media
 * (Song, Podcast, etc.)
 */
public abstract class Media extends BaseEntity implements Playable {

    private static final Logger LOGGER = LogManager.getLogger(Media.class);

    // Title of media (name of song or podcast)
    protected String title;

    // Duration in seconds
    protected int duration;

    // Static block – runs once when class is loaded
    static {
        LOGGER.info("Media class loaded into memory");
    }

    /**
     * Constructor to initialize media fields
     */
    public Media(int id, String title, int duration) {
        super(id);
        this.title = title;
        this.duration = duration;
    }

    /**
     * Returns a string representation of this media (subclass-specific).
     */
    public abstract String getMediaInfo();

    /**
     * Business method – simulate playing media
     */
    @Override
    public void play() {
        LOGGER.info("Playing: {}", title);
    }

    // --------- Getters ---------

    public String getTitle() {
        return title;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    // --------- Setters ---------

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
