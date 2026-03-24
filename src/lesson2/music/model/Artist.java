package lesson2.music.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a music artist in the system.
 * This is an independent domain entity (not inherited from Media).
 */
public class Artist implements Reviewable {

    // Artist stage or real name
    private String name;

    // Country of origin
    private String country;

    // Year when artist started career
    private int debutYear;

    // Artist's albums
    private List<Album> albums;

    // -------- Reviewable --------

    private List<Review> reviews;

    /**
     * Constructor to initialize artist fields
     */
    public Artist(String name, String country, int debutYear) {
        this.name = name;
        this.country = country;
        this.debutYear = debutYear;
        this.albums = new ArrayList<>();
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

    // ----------- Getters -----------

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public int getDebutYear() {
        return debutYear;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    // ----------- Setters -----------

    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setDebutYear(int debutYear) {
        this.debutYear = debutYear;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    /**
     * Business method – prints short info about artist
     */
    public void printArtistInfo() {
        System.out.println(name + " from " + country + " (since " + debutYear + ")");
    }
}
