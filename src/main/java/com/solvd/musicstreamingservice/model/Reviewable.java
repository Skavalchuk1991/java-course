package com.solvd.musicstreamingservice.model;

/**
 * Represents anything that can be reviewed by a user.
 */
public interface Reviewable {

    void addReview(Review review);

    double getAverageRating();

}
