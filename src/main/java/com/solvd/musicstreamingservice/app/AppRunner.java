package com.solvd.musicstreamingservice.app;

import com.solvd.musicstreamingservice.exception.*;
import com.solvd.musicstreamingservice.model.AppConstants;
import com.solvd.musicstreamingservice.model.*;
import com.solvd.musicstreamingservice.service.AudioStream;
import com.solvd.musicstreamingservice.service.MusicService;
import com.solvd.musicstreamingservice.service.PaymentService;
import com.solvd.musicstreamingservice.service.RatingSystem;
import com.solvd.musicstreamingservice.service.StreamingStatistics;
import com.solvd.musicstreamingservice.reflection.ReflectionDemo;
import com.solvd.musicstreamingservice.util.WordCounter;
import com.solvd.musicstreamingservice.pool.Connection;
import com.solvd.musicstreamingservice.pool.ConnectionPool;
import com.solvd.musicstreamingservice.pool.ConnectionRunnable;
import com.solvd.musicstreamingservice.pool.ConnectionThread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    private static final Logger LOGGER = LogManager.getLogger(AppRunner.class);

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
        LOGGER.info("Playlist duration: {} sec", playlist.calculateTotalDuration());

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
        LOGGER.info("Total streams: {}", StreamingStatistics.getTotalStreams());

        // Polymorphism: same method, different result for Song vs Podcast
        musicService.printMediaInfo(song1);
        musicService.printMediaInfo(podcast1);

        // Polymorphism via field: Media reference holds different subtypes
        musicService.setFeaturedMedia(song1);
        LOGGER.info("Featured: {}", musicService.getFeaturedMedia().getMediaInfo());
        musicService.setFeaturedMedia(podcast1);
        LOGGER.info("Featured: {}", musicService.getFeaturedMedia().getMediaInfo());

        // toString / equals / hashCode demo
        LOGGER.info("{}", user);
        LOGGER.info("{}", song1);
        LOGGER.info("song1.equals(song2): {}", song1.equals(song2));

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
        LOGGER.info("\n-- Playable interface (polymorphism) --");
        musicService.playItem(song1);      // Song implements Playable via Media
        musicService.playItem(podcast1);   // Podcast implements Playable via Media

        // Downloadable interface
        LOGGER.info("\n-- Downloadable interface --");
        song1.download();
        LOGGER.info("Is offline: {}", song1.isAvailableOffline());

        // Shareable interface
        LOGGER.info("\n-- Shareable interface --");
        musicService.shareItem(song1, newUser);

        // Subscribable interface
        LOGGER.info("\n-- Subscribable interface --");
        LOGGER.info("Has access: {}", song1.hasAccess(user));
        LOGGER.info("Required subscription: {}", song1.getRequiredSubscriptionType());

        // Reviewable interface
        LOGGER.info("\n-- Reviewable interface --");
        Review albumReview = new Review(user, song1, 4, "Great album!", LocalDateTime.now());
        album.addReview(albumReview);
        LOGGER.info("Album average rating: {}", album.getAverageRating());

        // final variable from AppConstants
        LOGGER.info("\n-- Final class and variables --");
        LOGGER.info("Max playlist size: {}", AppConstants.MAX_PLAYLIST_SIZE);
        LOGGER.info("Max download limit: {}", AppConstants.MAX_DOWNLOAD_LIMIT);

        // final method
        LOGGER.info("\n-- Final method --");
        LOGGER.info("Display name: {}", user.getDisplayName());

        // static variable and method
        LOGGER.info("\n-- Static variable and method --");
        LOGGER.info("Total registered users: {}", MusicService.getTotalRegisteredUsers());

        // --- Fix: each interface implemented by 2 explicit classes ---

        // Playable: Song vs Podcast (both explicit)
        LOGGER.info("\n-- Playable: Song vs Podcast --");
        musicService.playItem(song1);
        musicService.playItem(podcast1);

        // Downloadable: Song vs Podcast
        LOGGER.info("\n-- Downloadable: Song vs Podcast --");
        musicService.downloadItem(song1);
        musicService.downloadItem(podcast1);

        // Shareable: Song vs Playlist
        LOGGER.info("\n-- Shareable: Song vs Playlist --");
        musicService.shareItem(song1, newUser);
        musicService.shareItem(playlist, newUser);

        // Reviewable: Album vs Artist
        LOGGER.info("\n-- Reviewable: Album vs Artist --");
        Review artistReview = new Review(user, song1, 5, "Best artist!", LocalDateTime.now());
        artist.addReview(artistReview);
        musicService.printAverageRating(album);
        musicService.printAverageRating(artist);

        // Subscribable: Song vs Podcast
        LOGGER.info("\n-- Subscribable: Song vs Podcast --");
        LOGGER.info("Song access: {}", song1.hasAccess(user));
        LOGGER.info("Podcast access: {}", podcast1.hasAccess(user));

        LOGGER.info("Users in system:");
        for (User u : musicService.getUsers()) {
            LOGGER.debug("- {}", u.getUsername());
        }
        LOGGER.info("Media in catalog:");
        for (Media m : musicService.getCatalog()) {
            LOGGER.debug("- {}", m.getTitle());
        }

        // ============ HOMEWORK 5: Exceptions Demo ============

        LOGGER.info("\n===== HOMEWORK 5: EXCEPTIONS =====\n");

        // --- 1. Checked exception: MediaLoadException (handled with try-catch-finally) ---
        LOGGER.info("--- 1. Checked Exception: MediaLoadException ---");
        try {
            musicService.loadMediaFromSource("corrupted_file.mp3");
        } catch (MediaLoadException e) {
            LOGGER.error("Caught checked exception: {}", e.getMessage());
        } finally {
            LOGGER.warn("Finally block executed: cleanup after media load attempt");
        }

        // Also handle with try-catch using valid source
        try {
            musicService.loadMediaFromSource("valid_source.mp3");
        } catch (MediaLoadException e) {
            LOGGER.info("Caught checked exception: {}", e.getMessage());
        }

        user.setDownloadLimit(3); // reset for clean demo
        // --- 2. Unchecked: DownloadLimitExceededException ---
        LOGGER.info("\n--- 2. Unchecked: DownloadLimitExceededException ---");
        // Note: 'user' should be PremiumUser — use the existing PremiumUser variable from earlier code
        try {
            // Attempt downloads exceeding the limit (assuming downloadLimit was set during construction)
            for (int i = 0; i < 15; i++) {
                user.downloadSong(song1);
            }
        } catch (DownloadLimitExceededException e) {
            LOGGER.error("Caught: {}", e.getMessage());
        }

        // --- 3. Unchecked: PlaylistFullException ---
        LOGGER.info("\n--- 3. Unchecked: PlaylistFullException ---");
        try {
            // Create a playlist and fill it to MAX_PLAYLIST_SIZE using addItem, then add one more
            Playlist fullPlaylist = new Playlist("Full Playlist", new ArrayList<>());
            for (int i = 0; i < AppConstants.MAX_PLAYLIST_SIZE; i++) {
                fullPlaylist.addItem(song1);
            }
            // This 101st add should throw PlaylistFullException
            fullPlaylist.addItem(song1);
        } catch (PlaylistFullException e) {
            LOGGER.info("Caught: {}", e.getMessage());
        }

        // --- 4. Unchecked: UserNotFoundException ---
        LOGGER.info("\n--- 4. Unchecked: UserNotFoundException ---");
        try {
            musicService.streamMedia(null, song1);
        } catch (UserNotFoundException e) {
            LOGGER.info("Caught: {}", e.getMessage());
        }

        // --- 5. Unchecked: InvalidSubscriptionException ---
        LOGGER.info("\n--- 5. Unchecked: InvalidSubscriptionException ---");
        try {
            User noSubUser = new User(99, "ghost", "ghost@mail.com", null);
            musicService.streamMedia(noSubUser, song1);
        } catch (InvalidSubscriptionException e) {
            LOGGER.info("Caught: {}", e.getMessage());
        }

        // --- 6. AutoCloseable + try-with-resources ---
        LOGGER.info("\n--- 6. Try-with-resources: AudioStream ---");
        try (AudioStream audioStream = new AudioStream(song1)) {
            audioStream.stream();
            LOGGER.info("Stream is open: {}", audioStream.isOpen());
        } // audioStream.close() is called automatically here
        LOGGER.info("After try-with-resources block — stream was auto-closed");

        LOGGER.info("\n===== END OF HOMEWORK 5 =====");

        // ============ HOMEWORK 6: Collections & Generics Demo ============

        LOGGER.info("\n===== HOMEWORK 6: COLLECTIONS & GENERICS =====\n");

        // --- 1. List: popular methods ---
        LOGGER.info("--- 1. List operations ---");
        List<Song> favoriteSongs = new ArrayList<>();
        favoriteSongs.add(song1);
        favoriteSongs.add(song2);
        favoriteSongs.add(newSong);
        LOGGER.info("Size: {}", favoriteSongs.size());
        LOGGER.info("Is empty: {}", favoriteSongs.isEmpty());
        LOGGER.info("First element (get): {}", favoriteSongs.get(0));
        LOGGER.info("Contains song1: {}", favoriteSongs.contains(song1));
        favoriteSongs.remove(newSong);
        LOGGER.info("After remove, size: {}", favoriteSongs.size());

        // Iterate through List
        LOGGER.info("Iterating List:");
        for (Song s : favoriteSongs) {
            LOGGER.debug("  - {}", s.getTitle());
        }

        // Retrieve first element from List
        LOGGER.info("First from List: {}", favoriteSongs.get(0));

        // --- 2. Set: unique genres (custom class as value) ---
        LOGGER.info("\n--- 2. Set operations (Genre as custom class) ---");
        Genre rock = new Genre("Rock", "Rock music");
        Genre jazz = new Genre("Jazz", "Jazz music");
        Genre popDuplicate = new Genre("Pop", "Pop duplicate"); // same name as existing genre

        musicService.addGenre(genre);       // Pop
        musicService.addGenre(rock);        // Rock
        musicService.addGenre(jazz);        // Jazz
        musicService.addGenre(popDuplicate); // duplicate — should NOT be added

        Set<Genre> genres = musicService.getGenres();
        LOGGER.info("Genre set size (should be 3, not 4): {}", genres.size());
        LOGGER.info("Contains rock: {}", genres.contains(rock));

        // Iterate through Set
        LOGGER.info("Iterating Set:");
        for (Genre g : genres) {
            LOGGER.debug("  - {}", g.getName());
        }

        // Retrieve first element from Set
        Genre firstGenre = genres.iterator().next();
        LOGGER.info("First from Set: {}", firstGenre.getName());

        // --- 3. Map: listening history (User as custom key) ---
        LOGGER.info("\n--- 3. Map operations (User as custom key) ---");
        // streamMedia already records listening history
        musicService.streamMedia(user, song2);
        musicService.streamMedia(user, podcast1);

        Map<User, List<Media>> history = musicService.getListeningHistory();
        LOGGER.info("History map size: {}", history.size());
        LOGGER.info("History is empty: {}", history.isEmpty());

        List<Media> userHistory = musicService.getUserHistory(user);
        LOGGER.info("User '{}' listened to {} items", user.getUsername(), userHistory.size());

        // put — add history for newUser manually
        List<Media> newUserMedia = new ArrayList<>();
        newUserMedia.add(song1);
        history.put(newUser, newUserMedia);
        LOGGER.info("After put, history map size: {}", history.size());

        // Iterate through Map (entrySet)
        LOGGER.info("Iterating Map:");
        for (Map.Entry<User, List<Media>> entry : history.entrySet()) {
            LOGGER.debug("  User: {} -> {} tracks", entry.getKey().getUsername(), entry.getValue().size());
        }

        // Retrieve first element from Map
        Map.Entry<User, List<Media>> firstEntry = history.entrySet().iterator().next();
        LOGGER.info("First from Map: {}", firstEntry.getKey().getUsername());

        // --- 4. Generic classes ---
        LOGGER.info("\n--- 4. Generic classes ---");

        // Pair<Song, Integer> — song with play count
        Pair<Song, Integer> topSong = new Pair<>(song1, 150);
        LOGGER.info("Top song: {}", topSong);

        // Pair<User, Subscription> — user with their plan
        Pair<User, Subscription> userPlan = new Pair<>(user, premium);
        LOGGER.info("User plan: {}", userPlan);

        // RatingSystem<Song>
        RatingSystem<Song> songRatings = new RatingSystem<>();
        songRatings.addRating(song1, 5);
        songRatings.addRating(song2, 4);
        songRatings.addRating(newSong, 3);
        LOGGER.info("Song ratings count: {}", songRatings.size());
        LOGGER.info("Average song rating: {}", songRatings.getAverageRating());
        LOGGER.info("Rating system empty: {}", songRatings.isEmpty());

        // RatingSystem<Podcast> — same generic class, different type
        RatingSystem<Podcast> podcastRatings = new RatingSystem<>();
        podcastRatings.addRating(podcast1, 5);
        LOGGER.info("Podcast ratings count: {}", podcastRatings.size());

        // --- 5. Custom LinkedList ---
        LOGGER.info("\n--- 5. Custom LinkedList ---");
        CustomLinkedList<String> recentlyPlayed = new CustomLinkedList<>();
        recentlyPlayed.add("Blinding Lights");
        recentlyPlayed.add("Starboy");
        recentlyPlayed.add("Tech Talks");
        LOGGER.info("LinkedList size: {}", recentlyPlayed.size());
        LOGGER.info("Is empty: {}", recentlyPlayed.isEmpty());
        LOGGER.info("Get first: {}", recentlyPlayed.getFirst());
        LOGGER.info("Get by index(1): {}", recentlyPlayed.get(1));
        recentlyPlayed.remove("Starboy");
        LOGGER.info("After remove: {}", recentlyPlayed);

        // Iterate custom LinkedList
        LOGGER.info("Iterating CustomLinkedList:");
        for (String title : recentlyPlayed) {
            LOGGER.debug("  - {}", title);
        }

        LOGGER.info("\n===== END OF HOMEWORK 6 =====");

        // ============ HOMEWORK 7: Lambda, Enum, Record Demo ============

        LOGGER.info("\n===== HOMEWORK 7: LAMBDA, ENUM, RECORD =====\n");

        // --- 1. Lambdas from java.util.function (5 different) ---
        LOGGER.info("--- 1. Lambdas from java.util.function ---");

        // Predicate<Media> — filter songs longer than 200 sec
        Predicate<Media> longMedia = media -> media.getDuration() > 200;
        List<Media> longSongs = musicService.filterCatalog(longMedia);
        LOGGER.info("Media longer than 200s: {}", longSongs.size());

        // Consumer<Media> — print each media title
        Consumer<Media> printTitle = media -> LOGGER.info("  >> {}", media.getTitle());
        LOGGER.info("All catalog:");
        musicService.forEachMedia(printTitle);

        // Function<Media, String> — extract titles
        Function<Media, String> toTitle = media -> media.getTitle().toUpperCase();
        List<String> titles = musicService.mapCatalog(toTitle);
        LOGGER.info("Uppercase titles: {}", titles);

        // Supplier<Media> — default song when not found
        Supplier<Media> defaultSong = () -> new Song(0, "Unknown", 0, "Unknown", "Unknown", genre);
        Media found = musicService.getOrDefault(999, defaultSong);
        LOGGER.info("Get or default (id=999): {}", found.getTitle());

        // BiFunction<User, Media, String> — create a listening message
        BiFunction<User, Media, String> listenMessage = (listeningUser, media) ->
                listeningUser.getUsername() + " just listened to '" + media.getTitle() + "'";
        String msg = musicService.processUserMedia(user, song1, listenMessage);
        LOGGER.info("BiFunction result: {}", msg);

        // Runnable — schedule a background task (no input, no output)
        Runnable cacheCleanup = () -> LOGGER.info("Cache cleanup task executed");
        LOGGER.info("Running scheduled task:");
        cacheCleanup.run();

        // BiConsumer<User, Media> — perform action with two inputs, no return
        BiConsumer<User, Media> logListening = (listeningUser, media) ->
                LOGGER.info("LOG: {} listened to {}", listeningUser.getUsername(), media.getTitle());
        logListening.accept(user, song1);
        logListening.accept(user, podcast1);

        // --- 2. Custom functional interfaces with lambdas ---
        LOGGER.info("\n--- 2. Custom functional interfaces ---");

        // MediaFilter<Song> — filter songs by artist
        MediaFilter<Song> weekndFilter = song -> song.getArtist().equals("The Weeknd");
        LOGGER.info("Is song1 by The Weeknd? {}", weekndFilter.test(song1));

        // MediaTransformer<Song, String> — transform song to display string
        MediaTransformer<Song, String> songFormatter = song ->
                song.getTitle() + " by " + song.getArtist() + " (" + song.getDuration() + "s)";
        LOGGER.info("Formatted: {}", songFormatter.transform(song1));

        // MediaAction<Media> — action on media
        MediaAction<Media> playAction = media -> LOGGER.info("Custom action: Playing {}", media.getTitle());
        playAction.execute(song1);
        playAction.execute(podcast1);

        // --- 3. Complex Enums ---
        LOGGER.info("\n--- 3. Complex Enums ---");

        // SubscriptionTier
        LOGGER.info("Premium tier: {}", SubscriptionTier.PREMIUM.describe());
        LOGGER.info("Free offline access: {}", SubscriptionTier.FREE.hasOfflineAccess());
        for (SubscriptionTier tier : SubscriptionTier.values()) {
            LOGGER.debug("  {} — ${}", tier.name(), tier.getMonthlyPrice());
        }

        // MediaType
        LOGGER.info("\nMediaType SONG: {}", MediaType.SONG.getDescription());
        LOGGER.info("Is 5 min valid for SONG? {}", MediaType.SONG.isValidDuration(5));
        LOGGER.info("Is 5 min valid for AUDIOBOOK? {}", MediaType.AUDIOBOOK.isValidDuration(5));

        // PlaybackSpeed
        int original = song1.getDuration();
        LOGGER.info("\nOriginal duration: {}s", original);
        LOGGER.info("At 2x speed: {}s", PlaybackSpeed.DOUBLE.adjustDuration(original));
        LOGGER.info("At 0.5x speed: {}s", PlaybackSpeed.HALF.adjustDuration(original));

        // NotificationType
        LOGGER.info("\n{}", NotificationType.NEW_RELEASE.format("The Weeknd dropped a new album!"));
        LOGGER.info("Is SYSTEM high priority? {}", NotificationType.SYSTEM.isHighPriority());

        // RepeatMode — cycle through modes
        RepeatMode mode = RepeatMode.OFF;
        LOGGER.info("\nRepeat mode cycle:");
        for (int i = 0; i < 5; i++) {
            LOGGER.debug("  {} → {}", mode.name(), mode.getDescription());
            mode = mode.next();
        }

        // --- 4. Record ---
        LOGGER.info("\n--- 4. Record ---");
        TrackInfo track = new TrackInfo("Blinding Lights", "The Weeknd", 200, MediaType.SONG);
        LOGGER.info("TrackInfo: {}", track);
        LOGGER.info("Title: {}", track.title());
        LOGGER.info("Duration: {}", track.formattedDuration());
        LOGGER.info("Type: {}", track.type().getDescription());

        // Record equals — auto-generated
        TrackInfo track2 = new TrackInfo("Blinding Lights", "The Weeknd", 200, MediaType.SONG);
        LOGGER.info("track.equals(track2): {}", track.equals(track2));

        LOGGER.info("\n===== END OF HOMEWORK 7 =====");

        // ============ HOMEWORK 8: Streams, Reflection, Annotations ============

        LOGGER.info("\n===== HOMEWORK 8: STREAMS, REFLECTION, ANNOTATIONS =====\n");

        // --- 1. Stream operations demo ---
        LOGGER.info("--- Streams ---");
        LOGGER.info("Media longer than 200s: {}", musicService.countMediaLongerThan(200));
        LOGGER.info("Sorted titles: {}", musicService.getSortedTitles());
        LOGGER.info("Total catalog duration: {}s", musicService.getTotalCatalogDuration());

        // --- Optional handling ---
        LOGGER.info("\n--- Optional ---");
        Optional<Media> foundMedia = musicService.findMediaByTitle("Blinding Lights");
        foundMedia.ifPresent(media -> LOGGER.info("Found: {}", media.getTitle()));

        Optional<Media> missing = musicService.findMediaByTitle("Nonexistent Song");
        LOGGER.info("Missing media: {}", missing.orElse(new Song(0, "Fallback Song", 0, "Unknown", "Unknown", genre)));
        LOGGER.info("Is present: {}", missing.isPresent());

        // --- 2. Reflection + Annotation demo ---
        ReflectionDemo.run();

        LOGGER.info("\n===== END OF HOMEWORK 8 =====");

        // ============ HOMEWORK 10: Logging, StringUtils, FileUtils ============

        LOGGER.info("\n===== HOMEWORK 10: LOGGING, STRINGUTILS, FILEUTILS =====\n");

        // Word counter using StringUtils and FileUtils
        WordCounter.countUniqueWords("src/main/resources/book.txt", "src/main/resources/unique_words.txt");

        LOGGER.info("\n===== END OF HOMEWORK 10 =====");

        // ============ HOMEWORK 11: Threads, Connection Pool ============

        LOGGER.info("\n===== HOMEWORK 11: THREADS, CONNECTION POOL =====\n");

        // --- 1. Two threads: Thread subclass + Runnable ---
        LOGGER.info("--- 1. Thread subclass + Runnable ---");
        ConnectionPool pool = ConnectionPool.getInstance(5);

        // Thread created by extending Thread
        ConnectionThread thread1 = new ConnectionThread("ExtendThread-1", pool);

        // Thread created by implementing Runnable
        Thread thread2 = new Thread(new ConnectionRunnable(pool), "RunnableThread-1");

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            LOGGER.error("Main thread interrupted: {}", e.getMessage());
        }

        // --- 2. Connection Pool with ExecutorService (7 threads, 5 connections) ---
        LOGGER.info("\n--- 2. ExecutorService: 7 threads competing for 5 connections ---");

        java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newFixedThreadPool(7);

        // Submit 7 tasks — 5 will get connections immediately and 2 will wait
        for (int i = 1; i <= 7; i++) {
            final int taskId = i;
            executor.submit(() -> {
                Connection conn = pool.getConnection();
                try {
                    conn.create("Task-" + taskId + " data");
                    conn.get(taskId);
                    Thread.sleep(2000); // simulate work — hold connection for 2 seconds
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    pool.releaseConnection(conn);
                }
            });
        }

        // Shutdown executor and wait for all tasks to complete
        executor.shutdown();
        try {
            executor.awaitTermination(30, java.util.concurrent.TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOGGER.error("Executor interrupted: {}", e.getMessage());
        }

        // --- 3. CompletableFuture demos (5 examples) ---
        LOGGER.info("\n--- 3. CompletableFuture (with and without CompletionStage) ---");

        // 1. supplyAsync + thenApply (CompletionStage chain)
        java.util.concurrent.CompletableFuture<String> future1 = java.util.concurrent.CompletableFuture
                .supplyAsync(() -> {
                    LOGGER.info("Future1: Loading song data...");
                    return "Blinding Lights";
                })
                .thenApply(title -> {
                    LOGGER.info("Future1: Transforming title to uppercase");
                    return title.toUpperCase();
                });
        LOGGER.info("Future1 result: {}", future1.join());

        // 2. supplyAsync + thenAccept (consume result, no return)
        java.util.concurrent.CompletableFuture<Void> future2 = java.util.concurrent.CompletableFuture
                .supplyAsync(() -> "sergey")
                .thenAccept(username -> LOGGER.info("Future2: Welcome back, {}!", username));
        future2.join();

        // 3. supplyAsync + thenCombine (combine two futures)
        java.util.concurrent.CompletableFuture<String> songFuture = java.util.concurrent.CompletableFuture
                .supplyAsync(() -> "Blinding Lights");
        java.util.concurrent.CompletableFuture<String> artistFuture = java.util.concurrent.CompletableFuture
                .supplyAsync(() -> "The Weeknd");
        java.util.concurrent.CompletableFuture<String> future3 = songFuture
                .thenCombine(artistFuture, (song, performer) -> song + " by " + performer);
        LOGGER.info("Future3 combined: {}", future3.join());

        // 4. runAsync (no input, no output — fire and forget)
        java.util.concurrent.CompletableFuture<Void> future4 = java.util.concurrent.CompletableFuture
                .runAsync(() -> LOGGER.info("Future4: Background cache cleanup executed"));
        future4.join();

        // 5. supplyAsync + exceptionally (error handling)
        java.util.concurrent.CompletableFuture<String> future5 = java.util.concurrent.CompletableFuture
                .supplyAsync(() -> {
                    if (true) throw new RuntimeException("Simulated connection failure");
                    return "data";
                })
                .exceptionally(ex -> {
                    LOGGER.error("Future5: Caught error: {}", ex.getMessage());
                    return "fallback data";
                });
        LOGGER.info("Future5 result: {}", future5.join());

        LOGGER.info("All threads completed. Available connections: {}", pool.getAvailableCount());

        LOGGER.info("\n===== END OF HOMEWORK 11 =====");
    }
}
