package lesson2.music.app;

import lesson2.music.exception.*;
import lesson2.music.model.AppConstants;
import lesson2.music.model.*;
import lesson2.music.service.AudioStream;
import lesson2.music.service.MusicService;
import lesson2.music.service.PaymentService;
import lesson2.music.service.RatingSystem;
import lesson2.music.service.StreamingStatistics;
import lesson2.music.reflection.ReflectionDemo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

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
        artist.getAlbums().add(album);

        List<Artist> artists = new ArrayList<>();
        artists.add(artist);

        // ---------- 2. Catalog: media from artists' albums + other media ----------

        Podcast podcast1 = new Podcast(3, "Tech Talks", 1800, "John Doe", 5);
        List<Media> catalog = new ArrayList<>();
        catalog.add(song1);
        catalog.add(song2);
        catalog.add(podcast1);

        // ---------- 3. Users with their library, playlists, reviews, notifications ----------

        Subscription premium = new Subscription("Premium", new BigDecimal("9.99"));
        PremiumUser user = new PremiumUser(1, "sergey", "sergey@mail.com", premium, 3);

        Library library = new Library(user);
        library.addMedia(song1);
        library.addMedia(podcast1);
        user.setLibrary(library);

        List<Media> playlistItems = new ArrayList<>();
        playlistItems.add(song1);
        playlistItems.add(song2);
        Playlist playlist = new Playlist("My Playlist", playlistItems);
        user.getPlaylists().add(playlist);
        System.out.println("Playlist duration: " + playlist.calculateTotalDuration() + " sec");

        Review review = new Review(user, song1, 5, "Amazing song!", LocalDateTime.now());
        user.getReviews().add(review);
        review.printReview();

        Notification notification = new Notification(user, "New album released!", "INFO");
        user.getNotifications().add(notification);
        notification.send();

        List<User> users = new ArrayList<>();
        users.add(user);

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

        // ---------- 6. Dynamic add ----------

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

        // --- Fix: each interface implemented by 2 explicit classes ---

        // Playable: Song vs Podcast (both explicit)
        System.out.println("\n-- Playable: Song vs Podcast --");
        musicService.playItem(song1);
        musicService.playItem(podcast1);

        // Downloadable: Song vs Podcast
        System.out.println("\n-- Downloadable: Song vs Podcast --");
        musicService.downloadItem(song1);
        musicService.downloadItem(podcast1);

        // Shareable: Song vs Playlist
        System.out.println("\n-- Shareable: Song vs Playlist --");
        musicService.shareItem(song1, newUser);
        musicService.shareItem(playlist, newUser);

        // Reviewable: Album vs Artist
        System.out.println("\n-- Reviewable: Album vs Artist --");
        Review artistReview = new Review(user, song1, 5, "Best artist!", LocalDateTime.now());
        artist.addReview(artistReview);
        musicService.printAverageRating(album);
        musicService.printAverageRating(artist);

        // Subscribable: Song vs Podcast
        System.out.println("\n-- Subscribable: Song vs Podcast --");
        System.out.println("Song access: " + song1.hasAccess(user));
        System.out.println("Podcast access: " + podcast1.hasAccess(user));

        System.out.println("Users in system:");
        for (User u : musicService.getUsers()) {
            System.out.println("- " + u.getUsername());
        }
        System.out.println("Media in catalog:");
        for (Media m : musicService.getCatalog()) {
            System.out.println("- " + m.getTitle());
        }

        // ============ HOMEWORK 5: Exceptions Demo ============

        System.out.println("\n===== HOMEWORK 5: EXCEPTIONS =====\n");

        // --- 1. Checked exception: MediaLoadException (handled with try-catch-finally) ---
        System.out.println("--- 1. Checked Exception: MediaLoadException ---");
        try {
            musicService.loadMediaFromSource("corrupted_file.mp3");
        } catch (MediaLoadException e) {
            System.out.println("Caught checked exception: " + e.getMessage());
        } finally {
            System.out.println("Finally block executed: cleanup after media load attempt");
        }

        // Also handle with try-catch using valid source
        try {
            musicService.loadMediaFromSource("valid_source.mp3");
        } catch (MediaLoadException e) {
            System.out.println("Caught checked exception: " + e.getMessage());
        }

        user.setDownloadLimit(3); // reset for clean demo
        // --- 2. Unchecked: DownloadLimitExceededException ---
        System.out.println("\n--- 2. Unchecked: DownloadLimitExceededException ---");
        // Note: 'user' should be PremiumUser — use the existing PremiumUser variable from earlier code
        try {
            // Attempt downloads exceeding the limit (assuming downloadLimit was set during construction)
            for (int i = 0; i < 15; i++) {
                user.downloadSong(song1);
            }
        } catch (DownloadLimitExceededException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        // --- 3. Unchecked: PlaylistFullException ---
        System.out.println("\n--- 3. Unchecked: PlaylistFullException ---");
        try {
            // Create a playlist and fill it to MAX_PLAYLIST_SIZE using addItem, then add one more
            Playlist fullPlaylist = new Playlist("Full Playlist", new ArrayList<>());
            for (int i = 0; i < AppConstants.MAX_PLAYLIST_SIZE; i++) {
                fullPlaylist.addItem(song1);
            }
            // This 101st add should throw PlaylistFullException
            fullPlaylist.addItem(song1);
        } catch (PlaylistFullException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        // --- 4. Unchecked: UserNotFoundException ---
        System.out.println("\n--- 4. Unchecked: UserNotFoundException ---");
        try {
            musicService.streamMedia(null, song1);
        } catch (UserNotFoundException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        // --- 5. Unchecked: InvalidSubscriptionException ---
        System.out.println("\n--- 5. Unchecked: InvalidSubscriptionException ---");
        try {
            User noSubUser = new User(99, "ghost", "ghost@mail.com", null);
            musicService.streamMedia(noSubUser, song1);
        } catch (InvalidSubscriptionException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        // --- 6. AutoCloseable + try-with-resources ---
        System.out.println("\n--- 6. Try-with-resources: AudioStream ---");
        try (AudioStream audioStream = new AudioStream(song1)) {
            audioStream.stream();
            System.out.println("Stream is open: " + audioStream.isOpen());
        } // audioStream.close() is called automatically here
        System.out.println("After try-with-resources block — stream was auto-closed");

        System.out.println("\n===== END OF HOMEWORK 5 =====");

        // ============ HOMEWORK 6: Collections & Generics Demo ============

        System.out.println("\n===== HOMEWORK 6: COLLECTIONS & GENERICS =====\n");

        // --- 1. List: popular methods ---
        System.out.println("--- 1. List operations ---");
        List<Song> favoriteSongs = new ArrayList<>();
        favoriteSongs.add(song1);
        favoriteSongs.add(song2);
        favoriteSongs.add(newSong);
        System.out.println("Size: " + favoriteSongs.size());
        System.out.println("Is empty: " + favoriteSongs.isEmpty());
        System.out.println("First element (get): " + favoriteSongs.get(0));
        System.out.println("Contains song1: " + favoriteSongs.contains(song1));
        favoriteSongs.remove(newSong);
        System.out.println("After remove, size: " + favoriteSongs.size());

        // Iterate through List
        System.out.println("Iterating List:");
        for (Song s : favoriteSongs) {
            System.out.println("  - " + s.getTitle());
        }

        // Retrieve first element from List
        System.out.println("First from List: " + favoriteSongs.get(0));

        // --- 2. Set: unique genres (custom class as value) ---
        System.out.println("\n--- 2. Set operations (Genre as custom class) ---");
        Genre rock = new Genre("Rock", "Rock music");
        Genre jazz = new Genre("Jazz", "Jazz music");
        Genre popDuplicate = new Genre("Pop", "Pop duplicate"); // same name as existing genre

        musicService.addGenre(genre);       // Pop
        musicService.addGenre(rock);        // Rock
        musicService.addGenre(jazz);        // Jazz
        musicService.addGenre(popDuplicate); // duplicate — should NOT be added

        Set<Genre> genres = musicService.getGenres();
        System.out.println("Genre set size (should be 3, not 4): " + genres.size());
        System.out.println("Contains rock: " + genres.contains(rock));

        // Iterate through Set
        System.out.println("Iterating Set:");
        for (Genre g : genres) {
            System.out.println("  - " + g.getName());
        }

        // Retrieve first element from Set
        Genre firstGenre = genres.iterator().next();
        System.out.println("First from Set: " + firstGenre.getName());

        // --- 3. Map: listening history (User as custom key) ---
        System.out.println("\n--- 3. Map operations (User as custom key) ---");
        // streamMedia already records listening history
        musicService.streamMedia(user, song2);
        musicService.streamMedia(user, podcast1);

        Map<User, List<Media>> history = musicService.getListeningHistory();
        System.out.println("History map size: " + history.size());
        System.out.println("History is empty: " + history.isEmpty());

        List<Media> userHistory = musicService.getUserHistory(user);
        System.out.println("User '" + user.getUsername() + "' listened to " + userHistory.size() + " items");

        // put — add history for newUser manually
        List<Media> newUserMedia = new ArrayList<>();
        newUserMedia.add(song1);
        history.put(newUser, newUserMedia);
        System.out.println("After put, history map size: " + history.size());

        // Iterate through Map (entrySet)
        System.out.println("Iterating Map:");
        for (Map.Entry<User, List<Media>> entry : history.entrySet()) {
            System.out.println("  User: " + entry.getKey().getUsername() + " -> " + entry.getValue().size() + " tracks");
        }

        // Retrieve first element from Map
        Map.Entry<User, List<Media>> firstEntry = history.entrySet().iterator().next();
        System.out.println("First from Map: " + firstEntry.getKey().getUsername());

        // --- 4. Generic classes ---
        System.out.println("\n--- 4. Generic classes ---");

        // Pair<Song, Integer> — song with play count
        Pair<Song, Integer> topSong = new Pair<>(song1, 150);
        System.out.println("Top song: " + topSong);

        // Pair<User, Subscription> — user with their plan
        Pair<User, Subscription> userPlan = new Pair<>(user, premium);
        System.out.println("User plan: " + userPlan);

        // RatingSystem<Song>
        RatingSystem<Song> songRatings = new RatingSystem<>();
        songRatings.addRating(song1, 5);
        songRatings.addRating(song2, 4);
        songRatings.addRating(newSong, 3);
        System.out.println("Song ratings count: " + songRatings.size());
        System.out.println("Average song rating: " + songRatings.getAverageRating());
        System.out.println("Rating system empty: " + songRatings.isEmpty());

        // RatingSystem<Podcast> — same generic class, different type
        RatingSystem<Podcast> podcastRatings = new RatingSystem<>();
        podcastRatings.addRating(podcast1, 5);
        System.out.println("Podcast ratings count: " + podcastRatings.size());

        // --- 5. Custom LinkedList ---
        System.out.println("\n--- 5. Custom LinkedList ---");
        CustomLinkedList<String> recentlyPlayed = new CustomLinkedList<>();
        recentlyPlayed.add("Blinding Lights");
        recentlyPlayed.add("Starboy");
        recentlyPlayed.add("Tech Talks");
        System.out.println("LinkedList size: " + recentlyPlayed.size());
        System.out.println("Is empty: " + recentlyPlayed.isEmpty());
        System.out.println("Get first: " + recentlyPlayed.getFirst());
        System.out.println("Get by index(1): " + recentlyPlayed.get(1));
        recentlyPlayed.remove("Starboy");
        System.out.println("After remove: " + recentlyPlayed);

        // Iterate custom LinkedList
        System.out.println("Iterating CustomLinkedList:");
        for (String title : recentlyPlayed) {
            System.out.println("  - " + title);
        }

        System.out.println("\n===== END OF HOMEWORK 6 =====");

        // ============ HOMEWORK 7: Lambda, Enum, Record Demo ============

        System.out.println("\n===== HOMEWORK 7: LAMBDA, ENUM, RECORD =====\n");

        // --- 1. Lambdas from java.util.function (5 different) ---
        System.out.println("--- 1. Lambdas from java.util.function ---");

        // Predicate<Media> — filter songs longer than 200 sec
        Predicate<Media> longMedia = media -> media.getDuration() > 200;
        List<Media> longSongs = musicService.filterCatalog(longMedia);
        System.out.println("Media longer than 200s: " + longSongs.size());

        // Consumer<Media> — print each media title
        Consumer<Media> printTitle = media -> System.out.println("  >> " + media.getTitle());
        System.out.println("All catalog:");
        musicService.forEachMedia(printTitle);

        // Function<Media, String> — extract titles
        Function<Media, String> toTitle = media -> media.getTitle().toUpperCase();
        List<String> titles = musicService.mapCatalog(toTitle);
        System.out.println("Uppercase titles: " + titles);

        // Supplier<Media> — default song when not found
        Supplier<Media> defaultSong = () -> new Song(0, "Unknown", 0, "Unknown", "Unknown", genre);
        Media found = musicService.getOrDefault(999, defaultSong);
        System.out.println("Get or default (id=999): " + found.getTitle());

        // BiFunction<User, Media, String> — create a listening message
        BiFunction<User, Media, String> listenMessage = (listeningUser, media) ->
                listeningUser.getUsername() + " just listened to '" + media.getTitle() + "'";
        String msg = musicService.processUserMedia(user, song1, listenMessage);
        System.out.println("BiFunction result: " + msg);

        // Runnable — schedule a background task (no input, no output)
        Runnable cacheCleanup = () -> System.out.println("Cache cleanup task executed");
        System.out.println("Running scheduled task:");
        cacheCleanup.run();

        // BiConsumer<User, Media> — perform action with two inputs, no return
        BiConsumer<User, Media> logListening = (listeningUser, media) ->
                System.out.println("LOG: " + listeningUser.getUsername() + " listened to " + media.getTitle());
        logListening.accept(user, song1);
        logListening.accept(user, podcast1);

        // --- 2. Custom functional interfaces with lambdas ---
        System.out.println("\n--- 2. Custom functional interfaces ---");

        // MediaFilter<Song> — filter songs by artist
        MediaFilter<Song> weekndFilter = song -> song.getArtist().equals("The Weeknd");
        System.out.println("Is song1 by The Weeknd? " + weekndFilter.test(song1));

        // MediaTransformer<Song, String> — transform song to display string
        MediaTransformer<Song, String> songFormatter = song ->
                song.getTitle() + " by " + song.getArtist() + " (" + song.getDuration() + "s)";
        System.out.println("Formatted: " + songFormatter.transform(song1));

        // MediaAction<Media> — action on media
        MediaAction<Media> playAction = media -> System.out.println("Custom action: Playing " + media.getTitle());
        playAction.execute(song1);
        playAction.execute(podcast1);

        // --- 3. Complex Enums ---
        System.out.println("\n--- 3. Complex Enums ---");

        // SubscriptionTier
        System.out.println("Premium tier: " + SubscriptionTier.PREMIUM.describe());
        System.out.println("Free offline access: " + SubscriptionTier.FREE.hasOfflineAccess());
        for (SubscriptionTier tier : SubscriptionTier.values()) {
            System.out.println("  " + tier.name() + " — $" + tier.getMonthlyPrice());
        }

        // MediaType
        System.out.println("\nMediaType SONG: " + MediaType.SONG.getDescription());
        System.out.println("Is 5 min valid for SONG? " + MediaType.SONG.isValidDuration(5));
        System.out.println("Is 5 min valid for AUDIOBOOK? " + MediaType.AUDIOBOOK.isValidDuration(5));

        // PlaybackSpeed
        int original = song1.getDuration();
        System.out.println("\nOriginal duration: " + original + "s");
        System.out.println("At 2x speed: " + PlaybackSpeed.DOUBLE.adjustDuration(original) + "s");
        System.out.println("At 0.5x speed: " + PlaybackSpeed.HALF.adjustDuration(original) + "s");

        // NotificationType
        System.out.println("\n" + NotificationType.NEW_RELEASE.format("The Weeknd dropped a new album!"));
        System.out.println("Is SYSTEM high priority? " + NotificationType.SYSTEM.isHighPriority());

        // RepeatMode — cycle through modes
        RepeatMode mode = RepeatMode.OFF;
        System.out.println("\nRepeat mode cycle:");
        for (int i = 0; i < 5; i++) {
            System.out.println("  " + mode.name() + " → " + mode.getDescription());
            mode = mode.next();
        }

        // --- 4. Record ---
        System.out.println("\n--- 4. Record ---");
        TrackInfo track = new TrackInfo("Blinding Lights", "The Weeknd", 200, MediaType.SONG);
        System.out.println("TrackInfo: " + track);
        System.out.println("Title: " + track.title());
        System.out.println("Duration: " + track.formattedDuration());
        System.out.println("Type: " + track.type().getDescription());

        // Record equals — auto-generated
        TrackInfo track2 = new TrackInfo("Blinding Lights", "The Weeknd", 200, MediaType.SONG);
        System.out.println("track.equals(track2): " + track.equals(track2));

        System.out.println("\n===== END OF HOMEWORK 7 =====");

        // ============ HOMEWORK 8: Streams, Reflection, Annotations ============

        System.out.println("\n===== HOMEWORK 8: STREAMS, REFLECTION, ANNOTATIONS =====\n");

        // --- 1. Stream operations demo ---
        System.out.println("--- Streams ---");
        System.out.println("Media longer than 200s: " + musicService.countMediaLongerThan(200));
        System.out.println("Sorted titles: " + musicService.getSortedTitles());
        System.out.println("Total catalog duration: " + musicService.getTotalCatalogDuration() + "s");

        // --- Optional handling ---
        System.out.println("\n--- Optional ---");
        Optional<Media> foundMedia = musicService.findMediaByTitle("Blinding Lights");
        foundMedia.ifPresent(media -> System.out.println("Found: " + media.getTitle()));

        Optional<Media> missing = musicService.findMediaByTitle("Nonexistent Song");
        System.out.println("Missing media: " + missing.orElse(new Song(0, "Fallback Song", 0, "Unknown", "Unknown", genre)));
        System.out.println("Is present: " + missing.isPresent());

        // --- 2. Reflection + Annotation demo ---
        ReflectionDemo.run();

        System.out.println("\n===== END OF HOMEWORK 8 =====");
    }
}
