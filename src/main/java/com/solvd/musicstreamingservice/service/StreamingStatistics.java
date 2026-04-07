package com.solvd.musicstreamingservice.service;

/**
 * Tracks global streaming statistics
 */
public class StreamingStatistics {

    // Static variable – shared across all instances
    private static int totalStreams;

    // Static block – executed once when class is loaded
    static {
        totalStreams = 0;
        System.out.println("StreamingStatistics initialized");
    }

    /**
     * Increases stream counter
     */
    public static void increaseCounter() {
        totalStreams++;
    }

    /**
     * Returns total number of streams
     */
    public static int getTotalStreams() {
        return totalStreams;
    }
}