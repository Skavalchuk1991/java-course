package com.solvd.musicstreamingservice.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Enum for playlist repeat modes.
 */
public enum RepeatMode {
    OFF("No repeat", false, false),
    ONE("Repeat one track", true, false),
    ALL("Repeat entire playlist", true, true),
    SHUFFLE("Shuffle and repeat", true, true);

    private final String description;
    private final boolean repeating;
    private final boolean multiTrack;

    private static final Logger LOGGER = LogManager.getLogger(RepeatMode.class);

    static {
        LOGGER.info("RepeatMode options loaded");
    }

    RepeatMode(String description, boolean repeating, boolean multiTrack) {
        this.description = description;
        this.repeating = repeating;
        this.multiTrack = multiTrack;
    }

    public String getDescription() { return description; }
    public boolean isRepeating() { return repeating; }
    public boolean isMultiTrack() { return multiTrack; }

    public RepeatMode next() {
        RepeatMode[] modes = values();
        return modes[(this.ordinal() + 1) % modes.length];
    }
}
