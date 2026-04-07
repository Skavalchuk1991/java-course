package com.solvd.musicstreamingservice.model;

/**
 * Represents anything that can be shared.
 */
public interface Shareable {

    String getShareLink();

    void share(User recipient);

}
