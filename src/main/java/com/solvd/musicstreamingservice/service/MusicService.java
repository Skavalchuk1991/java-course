package com.solvd.musicstreamingservice.service;

import com.solvd.musicstreamingservice.exception.InvalidSubscriptionException;
import com.solvd.musicstreamingservice.exception.MediaLoadException;
import com.solvd.musicstreamingservice.exception.UserNotFoundException;
import com.solvd.musicstreamingservice.model.Artist;
import com.solvd.musicstreamingservice.model.Downloadable;
import com.solvd.musicstreamingservice.model.Genre;
import com.solvd.musicstreamingservice.model.Media;
import com.solvd.musicstreamingservice.model.Playable;
import com.solvd.musicstreamingservice.model.Reviewable;
import com.solvd.musicstreamingservice.model.Shareable;
import com.solvd.musicstreamingservice.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
        listeningHistory.computeIfAbsent(user, userKey -> new ArrayList<>()).add(media);
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

    /**
     * Lambda with Predicate: filters catalog by condition.
     */
    public List<Media> filterCatalog(Predicate<Media> condition) {
        return catalog.stream()
                .filter(condition)
                .collect(Collectors.toList());
    }

    /**
     * Lambda with Consumer: applies an action to each media in catalog.
     */
    public void forEachMedia(Consumer<Media> action) {
        catalog.forEach(action);
    }

    /**
     * Lambda with Function: transforms each media into something else.
     */
    public <R> List<R> mapCatalog(Function<Media, R> mapper) {
        return catalog.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    /**
     * Lambda with Supplier: provides a default media when needed.
     */
    public Media getOrDefault(int id, Supplier<Media> defaultSupplier) {
        return catalog.stream()
                .filter(media -> media.getId() == id)
                .findFirst()
                .orElseGet(defaultSupplier);
    }

    /**
     * Stream: count media longer than given duration.
     * Uses filter + count (terminal).
     */
    public long countMediaLongerThan(int seconds) {
        return catalog.stream()
                .filter(media -> media.getDuration() > seconds)
                .count();
    }

    /**
     * Stream: get sorted catalog titles.
     * Uses map + sorted + collect (non-terminal + terminal).
     */
    public List<String> getSortedTitles() {
        return catalog.stream()
                .map(Media::getTitle)
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Stream: get total duration of all media.
     * Uses mapToInt + sum (terminal).
     */
    public int getTotalCatalogDuration() {
        return catalog.stream()
                .mapToInt(Media::getDuration)
                .sum();
    }

    /**
     * Stream + Optional: find media by title.
     * Returns Optional to handle case when media is not found.
     */
    public Optional<Media> findMediaByTitle(String title) {
        return catalog.stream()
                .filter(media -> media.getTitle().equalsIgnoreCase(title))
                .findFirst();
    }

    /**
     * Lambda with BiFunction: combines user and media into a result.
     */
    public <R> R processUserMedia(User user, Media media, BiFunction<User, Media, R> processor) {
        return processor.apply(user, media);
    }
}
