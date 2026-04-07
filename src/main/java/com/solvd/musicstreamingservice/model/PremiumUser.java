package com.solvd.musicstreamingservice.model;

import com.solvd.musicstreamingservice.exception.DownloadLimitExceededException;

/**
 * PremiumUser is a special type of User
 * It extends User and adds extra functionality
 */
public class PremiumUser extends User {

    // Maximum number of downloads available
    private int downloadLimit;

    /**
     * Constructor for PremiumUser
     * Calls parent constructor using super()
     */
    public PremiumUser(int id, String username, String email, Subscription subscription, int downloadLimit) {
        super(id, username, email, subscription);
        this.downloadLimit = downloadLimit;
    }

    /**
     * Business method: allows downloading a song
     */
    public void downloadSong(Song song) {
        if (downloadLimit > 0) {
            downloadLimit--;
            System.out.println("Downloaded: " + song.getTitle());
        } else {
            throw new DownloadLimitExceededException("Download limit reached for user: " + getUsername());
        }
    }

    public int getDownloadLimit() {
        return downloadLimit;
    }

    public void setDownloadLimit(int downloadLimit) {
        this.downloadLimit = downloadLimit;
    }

    @Override
    public String toString() {
        return "PremiumUser{id=" + id + ", username='" + getUsername() + "', downloadLimit=" + downloadLimit + "}";
    }
}