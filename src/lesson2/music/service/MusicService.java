package lesson2.music.service;

import lesson2.music.exception.InvalidSubscriptionException;
import lesson2.music.exception.MediaLoadException;
import lesson2.music.exception.UserNotFoundException;
import lesson2.music.model.Artist;
import lesson2.music.model.Downloadable;
import lesson2.music.model.Genre;
import lesson2.music.model.Media;
import lesson2.music.model.Playable;
import lesson2.music.model.Reviewable;
import lesson2.music.model.Shareable;
import lesson2.music.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Core service of the music streaming system
 */
public class MusicService {

    // Static variable — shared across all instances
    private static int totalRegisteredUsers = 0;

    // Static block — runs once when class is loaded
    static {
        System.out.println("MusicService class initialized");
        totalRegisteredUsers = 0;
    }

    // Catalog of artists in the service
    private List<Artist> artists;

    // Catalog of available media (Songs and Podcasts)
    private List<Media> catalog;

    // Registered users
    private List<User> users;

    // Field with supertype — can hold any Media subtype (polymorphism)
    private Media featuredMedia;

    // Set of unique genres in the catalog (no duplicates)
    private Set<Genre> genres = new HashSet<>();

    // Map: user -> list of media they listened to (custom class as key)
    private Map<User, List<Media>> listeningHistory = new HashMap<>();

    /**
     * Constructor
     */
    public MusicService(List<Media> catalog, List<User> users) {
        this.artists = new ArrayList<>();
        this.catalog = catalog;
        this.users = users;
        totalRegisteredUsers = users.size();
    }

    // Static method
    public static int getTotalRegisteredUsers() {
        return totalRegisteredUsers;
    }

    public void addGenre(Genre genre) {
        genres.add(genre);
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void recordListening(User user, Media media) {
        listeningHistory.computeIfAbsent(user, k -> new ArrayList<>()).add(media);
    }

    public Map<User, List<Media>> getListeningHistory() {
        return listeningHistory;
    }

    public List<Media> getUserHistory(User user) {
        return listeningHistory.getOrDefault(user, new ArrayList<>());
    }

    /**
     * Business method:
     * Streams media for a user
     */
    public void streamMedia(User user, Media media) {
        if (user == null) {
            throw new UserNotFoundException("User not found: cannot stream media");
        }
        if (user.getSubscription() == null) {
            throw new InvalidSubscriptionException("User '" + user.getUsername() + "' has no valid subscription");
        }

        System.out.println(user.getUsername() + " is streaming: " + media.getTitle());

        // Call media play method (polymorphism)
        media.play();

        // Increase global streaming counter
        StreamingStatistics.increaseCounter();
        recordListening(user, media);
    }

    /**
     * Simulates loading media from an external source.
     * Throws checked MediaLoadException if media cannot be loaded.
     */
    public Media loadMediaFromSource(String source) throws MediaLoadException {
        if (source == null || source.isEmpty()) {
            throw new MediaLoadException("Cannot load media: source is null or empty");
        }
        // Simulate a loading failure for demonstration
        if (source.contains("corrupted")) {
            throw new MediaLoadException("Cannot load media: source '" + source + "' is corrupted");
        }
        System.out.println("Media loaded successfully from: " + source);
        return null; // In real code, would return actual media
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public List<Media> getCatalog() {
        return catalog;
    }

    public List<User> getUsers() {
        return users;
    }

    public Media getFeaturedMedia() {
        return featuredMedia;
    }

    public void setFeaturedMedia(Media featuredMedia) {
        this.featuredMedia = featuredMedia;
    }

    /**
     * Demonstrates polymorphism: works with any Media subtype.
     * Calls overridden getMediaInfo() — different result for Song vs Podcast.
     */
    public void printMediaInfo(Media media) {
        System.out.println(media.getMediaInfo());
    }

    /**
     * Polymorphism via interface:
     * Works with any object that implements Playable — Song, Podcast, etc.
     */
    public void playItem(Playable item) {
        System.out.println("Duration: " + item.getDuration() + " sec");
        item.play();
    }

    /**
     * Polymorphism via interface:
     * Works with any object that implements Shareable.
     */
    public void shareItem(Shareable item, User recipient) {
        System.out.println("Share link: " + item.getShareLink());
        item.share(recipient);
    }

    /**
     * Polymorphism via Downloadable interface.
     * Works with any class that explicitly implements Downloadable.
     */
    public void downloadItem(Downloadable item) {
        item.download();
        System.out.println("Available offline: " + item.isAvailableOffline());
    }

    /**
     * Polymorphism via Reviewable interface.
     * Works with any class that explicitly implements Reviewable.
     */
    public void printAverageRating(Reviewable item) {
        System.out.println("Average rating: " + item.getAverageRating());
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public void setCatalog(List<Media> catalog) {
        this.catalog = catalog;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    /**
     * Adds new artist to the service.
     */
    public void addArtist(Artist newArtist) {
        artists.add(newArtist);
    }

    /**
     * Adds new user to the system.
     */
    public void addUser(User newUser) {
        users.add(newUser);
        totalRegisteredUsers++;
    }

    /**
     * Adds new media to catalog.
     */
    public void addMedia(Media newMedia) {
        catalog.add(newMedia);
    }
}
