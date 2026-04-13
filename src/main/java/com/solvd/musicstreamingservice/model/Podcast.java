package com.solvd.musicstreamingservice.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Podcast is another type of Media
 */
public class Podcast extends Media implements Playable, Downloadable, Subscribable {

    private static final Logger LOGGER = LogManager.getLogger(Podcast.class);

    // Host of the podcast
    private String host;

    // Episode number
    private int episodeNumber;

    // -------- Downloadable --------

    private boolean availableOffline = false;

    /**
     * Constructor for Podcast
     */
    public Podcast(int id, String title, int duration, String host, int episodeNumber) {
        super(id, title, duration);
        this.host = host;
        this.episodeNumber = episodeNumber;
    }

    @Override
    public void play() {
        super.play();
    }

    @Override
    public int getDuration() {
        return super.getDuration();
    }

    @Override
    public void download() {
        this.availableOffline = true;
        LOGGER.info("Podcast '{}' downloaded for offline use", title);
    }

    @Override
    public boolean isAvailableOffline() {
        return availableOffline;
    }

    // -------- Subscribable --------

    @Override
    public boolean hasAccess(User user) {
        return user.getSubscription() != null;
    }

    @Override
    public String getRequiredSubscriptionType() {
        return "Basic";
    }

    @Override
    public String getMediaInfo() {
        return "Podcast: " + title + " | Host: " + host + " | Episode: " + episodeNumber;
    }

    @Override
    public String toString() {
        return "Podcast{title='" + title + "', host='" + host + "', episode=" + episodeNumber + "}";
    }

    // -------- Getters --------

    public String getHost() {
        return host;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    // -------- Setters ---------

    public void setHost(String host) {
        this.host = host;
    }

    public void setEpisodeNumber(int episodeNumber) {
        this.episodeNumber = episodeNumber;
    }
}
