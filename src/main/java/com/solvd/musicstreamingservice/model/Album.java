package com.solvd.musicstreamingservice.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a music album.
 * Album belongs to an Artist and contains songs.
 */
public class Album implements Reviewable {

    private static final Logger LOGGER = LogManager.getLogger(Album.class);

    // Album title
    private String title;

    // Release date (LocalDate as required)
    private LocalDate releaseDate;

    // Artist of this album
    private Artist artist;

    // Songs in this album
    private List<Song> songs;

    private List<Review> reviews;

    /**
     * Constructor to initialize album fields
     */
    public Album(String title, LocalDate releaseDate, Artist artist) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.artist = artist;
        this.songs = new ArrayList<>();
        this.reviews = new ArrayList<>();
    }

    @Override
    public void addReview(Review review) {
        reviews.add(review);
    }

    @Override
    public double getAverageRating() {
        return reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
    }

    public List<Review> getReviews() {
        return reviews;
    }

    // ---------- Getters ----------

    public String getTitle() {
        return title;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public Artist getArtist() {
        return artist;
    }

    public List<Song> getSongs() {
        return songs;
    }

    // ---------- Business Method ----------

    /**
     * Adds a song to the album.
     */
    public void addSong(Song song) {
        songs.add(song);
    }

    /**
     * Prints album information
     */
    public void printAlbumInfo() {
        LOGGER.info("Album: {} ({})", title, releaseDate);
        LOGGER.info("Artist: {}", artist.getName());
        LOGGER.info("Total songs: {}", songs.size());
    }

    // ---------- Setters ----------

    public void setTitle(String title) {
        this.title = title;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
