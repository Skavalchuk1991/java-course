package lesson2.music.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a music album.
 * Album belongs to an Artist and contains songs.
 */
public class Album implements Reviewable {

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
        if (reviews.isEmpty()) return 0.0;
        int total = 0;
        for (Review r : reviews) {
            total += r.getRating();
        }
        return (double) total / reviews.size();
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
        System.out.println("Album: " + title + " (" + releaseDate + ")");
        System.out.println("Artist: " + artist.getName());
        System.out.println("Total songs: " + songs.size());
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
