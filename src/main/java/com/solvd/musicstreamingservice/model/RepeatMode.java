package com.solvd.musicstreamingservice.model;

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

    static {
        System.out.println("RepeatMode options loaded");
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
