package com.solvd.musicstreamingservice.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Tracks global streaming statistics
 */
public class StreamingStatistics {

    private static final Logger LOGGER = LogManager.getLogger(StreamingStatistics.class);

    // Static variable – shared across all instances
    private static int totalStreams;

    // Static block – executed once when class is loaded
    static {
        totalStreams = 0;
        LOGGER.info("StreamingStatistics initialized");
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