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
        Song song1 = new Song("Blinding Lights", 200, "The Weeknd", "After Hours", genre);
        Song song2 = new Song("Starboy", 210, "The Weeknd", "Starboy", genre);

        Album album = new Album("After Hours", LocalDate.of(2020, 3, 20), artist);
        album.addSong(song1);
        album.addSong(song2);
        artist.setAlbums(new Album[]{album});

        Artist[] artists = {artist};

        // ---------- 2. Catalog: media from artists' albums + other media ----------

        Podcast podcast1 = new Podcast("Tech Talks", 1800, "John Doe", 5);
        Media[] catalog = {song1, song2, podcast1};

        // ---------- 3. Users with their library, playlists, reviews, notifications ----------

        Subscription premium = new Subscription("Premium", new BigDecimal("9.99"));
        PremiumUser user = new PremiumUser("sergey", "sergey@mail.com", premium, 3);

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
        User newUser = new User("alex", "alex@mail.com", basic);
        Library newUserLibrary = new Library(newUser);
        newUser.setLibrary(newUserLibrary);
        musicService.addUser(newUser);

        Song newSong = new Song("Save Your Tears", 210, "The Weeknd", "After Hours", genre);
        musicService.addMedia(newSong);

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
