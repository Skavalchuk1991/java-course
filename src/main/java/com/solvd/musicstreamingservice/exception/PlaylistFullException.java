package com.solvd.musicstreamingservice.exception;

public class PlaylistFullException extends RuntimeException {
    public PlaylistFullException(String message) {
        super(message);
    }
}
