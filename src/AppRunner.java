import lesson2.music.model.AppConstants;
import lesson2.music.model.*;
import lesson2.music.service.MusicService;
import lesson2.music.service.PaymentService;
import lesson2.music.service.StreamingStatistics;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entry point of the Music Streaming Service application.
 * All objects are created through the hierarchy: MusicService is the root,
 * everything flows down from it like a tree (artists with albums, users with library/playlists/reviews/notifications).
 */
public class AppRunner {

    public static void main(String[] args) {

        // ---------- 1. Build tree from root: Artists (with Albums and Songs) ----------

        Genre genre = new Genre("Pop", "Popular modern music genre");
        genre.printGenreInfo();

        Artist artist = new Artist("The Weeknd", "Canada", 2010);
        Song song1 = new Song(1, "Blinding Lights", 200, "The Weeknd", "After Hours", genre);
        Song song2 = new Song(2, "Starboy", 210, "The Weeknd", "Starboy", genre);

        Album album = new Album("After Hours", LocalDate.of(2020, 3, 20), artist);
        album.addSong(song1);
        album.addSong(song2);
        artist.setAlbums(new Album[]{album});

        Artist[] artists = {artist};

        // ---------- 2. Catalog: media from artists' albums + other media ----------

        Podcast podcast1 = new Podcast(3, "Tech Talks", 1800, "John Doe", 5);
        Media[] catalog = {song1, song2, podcast1};

        // ---------- 3. Users with their library, playlists, reviews, notifications ----------

        Subscription premium = new Subscription("Premium", new BigDecimal("9.99"));
        PremiumUser user = new PremiumUser(1, "sergey", "sergey@mail.com", premium, 3);

        Library library = new Library(user);
        library.addMedia(song1);
        library.addMedia(podcast1);
        user.setLibrary(library);

        Media[] playlistItems = {song1, song2};
        Playlist playlist = new Playlist("My Playlist", playlistItems);
        user.setPlaylists(new Playlist[]{playlist});
        System.out.println("Playlist duration: " + playlist.calculateTotalDuration() + " sec");

        Review review = new Review(user, song1, 5, "Amazing song!", LocalDateTime.now());
        user.setReviews(new Review[]{review});
        review.printReview();

        Notification notification = new Notification(user, "New album released!", "INFO");
        user.setNotifications(new Notification[]{notification});
        notification.send();

        User[] users = {user};

        // ---------- 4. Root: MusicService with full hierarchy ----------

        MusicService musicService = new MusicService(catalog, users);
        musicService.setArtists(artists);

        PaymentService paymentService = new PaymentService("Stripe");

        musicService.streamMedia(user, song1);
        user.downloadSong(song1);
        paymentService.processPayment(premium.getMonthlyPrice());
        System.out.println("Total streams: " + StreamingStatistics.getTotalStreams());

        // Polymorphism: same method, different result for Song vs Podcast
        musicService.printMediaInfo(song1);
        musicService.printMediaInfo(podcast1);

        // Polymorphism via field: Media reference holds different subtypes
        musicService.setFeaturedMedia(song1);
        System.out.println("Featured: " + musicService.getFeaturedMedia().getMediaInfo());
        musicService.setFeaturedMedia(podcast1);
        System.out.println("Featured: " + musicService.getFeaturedMedia().getMediaInfo());

        // toString / equals / hashCode demo
        System.out.println(user.toString());
        System.out.println(song1.toString());
        System.out.println("song1.equals(song2): " + song1.equals(song2));

        // ---------- 5. Access hierarchy from root: library, album info ----------

        for (User u : musicService.getUsers()) {
            u.getLibrary().printLibrary();
        }
        for (Artist a : musicService.getArtists()) {
            for (Album alb : a.getAlbums()) {
                alb.printAlbumInfo();
            }
        }

        // ---------- 6. Dynamic add (still only arrays) ----------

        Subscription basic = new Subscription("Basic", new BigDecimal("4.99"));
        User newUser = new User(2, "alex", "alex@mail.com", basic);
        Library newUserLibrary = new Library(newUser);
        newUser.setLibrary(newUserLibrary);
        musicService.addUser(newUser);

        Song newSong = new Song(4, "Save Your Tears", 210, "The Weeknd", "After Hours", genre);
        musicService.addMedia(newSong);

        // --- Lesson 4: Interfaces, static, final demo ---

        // Polymorphism via Playable interface
        System.out.println("\n-- Playable interface (polymorphism) --");
        musicService.playItem(song1);      // Song implements Playable via Media
        musicService.playItem(podcast1);   // Podcast implements Playable via Media

        // Downloadable interface
        System.out.println("\n-- Downloadable interface --");
        song1.download();
        System.out.println("Is offline: " + song1.isAvailableOffline());

        // Shareable interface
        System.out.println("\n-- Shareable interface --");
        musicService.shareItem(song1, newUser);

        // Subscribable interface
        System.out.println("\n-- Subscribable interface --");
        System.out.println("Has access: " + song1.hasAccess(user));
        System.out.println("Required subscription: " + song1.getRequiredSubscriptionType());

        // Reviewable interface
        System.out.println("\n-- Reviewable interface --");
        Review albumReview = new Review(user, song1, 4, "Great album!", LocalDateTime.now());
        album.addReview(albumReview);
        System.out.println("Album average rating: " + album.getAverageRating());

        // final variable from AppConstants
        System.out.println("\n-- Final class and variables --");
        System.out.println("Max playlist size: " + AppConstants.MAX_PLAYLIST_SIZE);
        System.out.println("Max download limit: " + AppConstants.MAX_DOWNLOAD_LIMIT);

        // final method
        System.out.println("\n-- Final method --");
        System.out.println("Display name: " + user.getDisplayName());

        // static variable and method
        System.out.println("\n-- Static variable and method --");
        System.out.println("Total registered users: " + MusicService.getTotalRegisteredUsers());

        System.out.println("Users in system:");
        for (User u : musicService.getUsers()) {
            System.out.println("- " + u.getUsername());
        }
        System.out.println("Media in catalog:");
        for (Media m : musicService.getCatalog()) {
            System.out.println("- " + m.getTitle());
        }
    }
}
