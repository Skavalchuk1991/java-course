package lesson2.music.model;

import java.time.LocalDate;

/**
 * Represents a music album.
 * Album belongs to an Artist and contains songs.
 */
public class Album {

    // Album title
    private String title;

    // Release date (LocalDate as required)
    private LocalDate releaseDate;

    // Artist of this album
    private Artist artist;

    // Array of songs in this album
    private Song[] songs;

    /**
     * Constructor to initialize album fields
     */
    public Album(String title, LocalDate releaseDate, Artist artist) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.artist = artist;
        this.songs = new Song[0];
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

    public Song[] getSongs() {
        return songs;
    }

    // ---------- Business Method ----------

    /**
     * Adds a song to the album.
     */
    public void addSong(Song song) {
        Song[] newSongs = new Song[songs.length + 1];
        System.arraycopy(songs, 0, newSongs, 0, songs.length);
        newSongs[songs.length] = song;
        songs = newSongs;
    }

    /**
     * Prints album information
     */
    public void printAlbumInfo() {
        System.out.println("Album: " + title + " (" + releaseDate + ")");
        System.out.println("Artist: " + artist.getName());
        System.out.println("Total songs: " + songs.length);
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

    public void setSongs(Song[] songs) {
        this.songs = songs;
    }
}
