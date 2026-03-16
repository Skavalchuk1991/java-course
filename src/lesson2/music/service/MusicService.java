package lesson2.music.service;

import lesson2.music.model.Artist;
import lesson2.music.model.Media;
import lesson2.music.model.User;

/**
 * Core service of the music streaming system
 */
public class MusicService {

    // Catalog of artists in the service
    private Artist[] artists;

    // Catalog of available media (Songs and Podcasts)
    private Media[] catalog;

    // Registered users
    private User[] users;

    // Field with supertype — can hold any Media subtype (polymorphism)
    private Media featuredMedia;

    /**
     * Constructor
     */
    public MusicService(Media[] catalog, User[] users) {
        this.artists = new Artist[0];
        this.catalog = catalog;
        this.users = users;
    }

    /**
     * Business method:
     * Streams media for a user
     */
    public void streamMedia(User user, Media media) {

        System.out.println(user.getUsername() + " is streaming: " + media.getTitle());

        // Call media play method (polymorphism)
        media.play();

        // Increase global streaming counter
        StreamingStatistics.increaseCounter();
    }

    public Artist[] getArtists() {
        return artists;
    }

    public Media[] getCatalog() {
        return catalog;
    }

    public User[] getUsers() {
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

    public void setArtists(Artist[] artists) {
        this.artists = artists;
    }

    public void setCatalog(Media[] catalog) {
        this.catalog = catalog;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }

    /**
     * Adds new artist to the service.
     * Creates a new array and copies existing artists into it.
     */
    public void addArtist(Artist newArtist) {
        Artist[] newArtists = new Artist[artists.length + 1];
        System.arraycopy(artists, 0, newArtists, 0, artists.length);
        newArtists[artists.length] = newArtist;
        artists = newArtists;
    }

    /**
     * Adds new user to the system.
     * Since arrays have fixed size, we create a new array
     * and copy existing users into it.
     */
    public void addUser(User newUser) {

        User[] newUsers = new User[users.length + 1];

        System.arraycopy(users, 0, newUsers, 0, users.length);

        newUsers[users.length] = newUser;

        users = newUsers;
    }

    /**
     * Adds new media to catalog.
     * Creates a new array and copies old media items.
     */
    public void addMedia(Media newMedia) {

        Media[] newCatalog = new Media[catalog.length + 1];

        System.arraycopy(catalog, 0, newCatalog, 0, catalog.length);

        newCatalog[catalog.length] = newMedia;

        catalog = newCatalog;
    }
}