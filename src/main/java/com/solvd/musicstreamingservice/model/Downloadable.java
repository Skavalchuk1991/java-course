package com.solvd.musicstreamingservice.model;

/**
 * Represents anything that can be downloaded.
 */
public interface Downloadable {

    void download();

    boolean isAvailableOffline();

}
