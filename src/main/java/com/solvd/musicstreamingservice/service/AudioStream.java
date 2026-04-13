package com.solvd.musicstreamingservice.service;

import com.solvd.musicstreamingservice.model.Media;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents an audio streaming connection that must be closed after use.
 * Implements AutoCloseable for use with try-with-resources.
 */
public class AudioStream implements AutoCloseable {

    private static final Logger LOGGER = LogManager.getLogger(AudioStream.class);

    private Media media;
    private boolean isOpen;

    public AudioStream(Media media) {
        this.media = media;
        this.isOpen = true;
        LOGGER.info("AudioStream opened for: {}", media.getTitle());
    }

    /**
     * Streams audio data. Throws IllegalStateException if stream is already closed.
     */
    public void stream() {
        if (!isOpen) {
            throw new IllegalStateException("AudioStream is already closed");
        }
        LOGGER.info("Streaming audio: {} ({} sec)", media.getTitle(), media.getDuration());
    }

    public boolean isOpen() {
        return isOpen;
    }

    public Media getMedia() {
        return media;
    }

    /**
     * Closes the audio stream. Called automatically in try-with-resources.
     */
    @Override
    public void close() {
        if (isOpen) {
            isOpen = false;
            LOGGER.info("AudioStream closed for: {}", media.getTitle());
        }
    }
}
