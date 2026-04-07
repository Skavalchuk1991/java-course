package com.solvd.musicstreamingservice.model;

/**
 * Enum for playback speed options.
 */
public enum PlaybackSpeed {
    HALF(0.5, "Slow"),
    NORMAL(1.0, "Normal"),
    ONE_AND_HALF(1.5, "Fast"),
    DOUBLE(2.0, "Very Fast");

    private final double multiplier;
    private final String label;

    static {
        System.out.println("PlaybackSpeed options loaded");
    }

    PlaybackSpeed(double multiplier, String label) {
        this.multiplier = multiplier;
        this.label = label;
    }

    public double getMultiplier() { return multiplier; }
    public String getLabel() { return label; }

    public int adjustDuration(int originalSeconds) {
        return (int) (originalSeconds / multiplier);
    }
}
