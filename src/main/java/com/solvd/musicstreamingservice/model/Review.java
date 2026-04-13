package com.solvd.musicstreamingservice.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;

/**
 * Represents a user review for media (song or album).
 */
public class Review {

    private static final Logger LOGGER = LogManager.getLogger(Review.class);

    // User who wrote the review
    private User user;

    // Media that is being reviewed
    private Media media;

    // Rating from 1 to 5
    private int rating;

    // Text comment
    private String comment;

    // When the review was created (LocalDateTime as required)
    private LocalDateTime reviewedAt;

    /**
     * Constructor to initialize review fields
     */
    public Review(User user, Media media, int rating, String comment, LocalDateTime reviewedAt) {
        this.user = user;
        this.media = media;
        this.rating = rating;
        this.comment = comment;
        this.reviewedAt = reviewedAt;
    }

    // -------- Getters --------

    public User getUser() {
        return user;
    }

    public Media getMedia() {
        return media;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    // -------- Setters --------

    public void setUser(User user) {
        this.user = user;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    /**
     * Business method – prints review information
     */
    public void printReview() {
        LOGGER.info("User: {}", user.getUsername());
        LOGGER.info("Media: {}", media.getTitle());
        LOGGER.info("Rating: {}", rating);
        LOGGER.info("Comment: {}", comment);
        LOGGER.info("Reviewed at: {}", reviewedAt);
    }
}
