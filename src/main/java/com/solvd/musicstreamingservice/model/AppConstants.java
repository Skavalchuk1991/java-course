package com.solvd.musicstreamingservice.model;

/**
 * Final class — cannot be extended.
 * Holds application-wide constants.
 */
public final class AppConstants {

    // final variable — cannot be reassigned
    public static final int MAX_PLAYLIST_SIZE = 100;
    public static final int MAX_DOWNLOAD_LIMIT = 10;
    public static final String DEFAULT_SHARE_BASE_URL = "https://music.app/";
    public static final String FREE_TIER_NAME = "Free";

    // private constructor — utility class, no instantiation needed
    private AppConstants() {}
}
