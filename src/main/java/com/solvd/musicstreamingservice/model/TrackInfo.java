package com.solvd.musicstreamingservice.model;

/**
 * Record — immutable data carrier for track information.
 * Automatically generates constructor, getters, equals, hashCode, toString.
 */
public record TrackInfo(String title, String artist, int durationSeconds, MediaType type) {

    /**
     * Custom method — returns formatted duration as mm:ss
     */
    public String formattedDuration() {
        int min = durationSeconds / 60;
        int sec = durationSeconds % 60;
        return String.format("%d:%02d", min, sec);
    }
}
