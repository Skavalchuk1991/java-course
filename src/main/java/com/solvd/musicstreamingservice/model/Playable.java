package com.solvd.musicstreamingservice.model;

/**
 * Represents anything that can be played.
 */
public interface Playable {

    void play();

    int getDuration();

}
