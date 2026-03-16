package lesson2.music.model;

import java.util.Objects;

/**
 * Represents a system user
 */
public class User extends BaseEntity {

    // Username of the user
    private String username;

    // Email address
    private String email;

    // Each user has a subscription (composition)
    private Subscription subscription;

    // Personal media library
    private Library library;

    // Personal playlists
    private Playlist[] playlists;

    // Reviews written by user
    private Review[] reviews;

    // Notifications for user
    private Notification[] notifications;

    /**
     * Constructor to initialize user
     */
    public User(int id, String username, String email, Subscription subscription) {
        super(id);
        this.username = username;
        this.email = email;
        this.subscription = subscription;
        this.playlists = new Playlist[0];
        this.reviews = new Review[0];
        this.notifications = new Notification[0];
    }

    // -------- Getters --------

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public Library getLibrary() {
        return library;
    }

    public Playlist[] getPlaylists() {
        return playlists;
    }

    public Review[] getReviews() {
        return reviews;
    }

    public Notification[] getNotifications() {
        return notifications;
    }

    // -------- Setters --------

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }

    public void setPlaylists(Playlist[] playlists) {
        this.playlists = playlists;
    }

    public void setReviews(Review[] reviews) {
        this.reviews = reviews;
    }

    public void setNotifications(Notification[] notifications) {
        this.notifications = notifications;
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', email='" + email + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }
}