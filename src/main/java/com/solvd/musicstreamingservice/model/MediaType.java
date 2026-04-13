package com.solvd.musicstreamingservice.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Enum representing types of media content.
 */
public enum MediaType {
    SONG("Audio track", 3, 7),
    PODCAST("Audio podcast", 15, 120),
    AUDIOBOOK("Audio book", 30, 600),
    LIVE_RECORDING("Live performance", 5, 30),
    REMIX("Remixed track", 3, 10);

    private final String description;
    private final int minDurationMinutes;
    private final int maxDurationMinutes;

    private static final Logger LOGGER = LogManager.getLogger(MediaType.class);

    static {
        LOGGER.info("MediaType enum loaded with {} types", values().length);
    }

    MediaType(String description, int minDurationMinutes, int maxDurationMinutes) {
        this.description = description;
        this.minDurationMinutes = minDurationMinutes;
        this.maxDurationMinutes = maxDurationMinutes;
    }

    public String getDescription() { return description; }
    public int getMinDurationMinutes() { return minDurationMinutes; }
    public int getMaxDurationMinutes() { return maxDurationMinutes; }

    public boolean isValidDuration(int minutes) {
        return minutes >= minDurationMinutes && minutes <= maxDurationMinutes;
    }
}
