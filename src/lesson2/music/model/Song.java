package lesson2.music.model;

import java.util.Objects;

/**
 * Song is a specific type of Media
 */
public class Song extends Media {

    // Artist of the song
    private String artist;

    // Album name
    private String album;

    // Genre of the song (links Song to Genre in hierarchy)
    private Genre genre;

    /**
     * Constructor for Song. Calls parent constructor using super()
     */
    public Song(String title, int duration, String artist, String album, Genre genre) {
        super(title, duration);
        this.artist = artist;
        this.album = album;
        this.genre = genre;
    }

    @Override
    public String getMediaInfo() {
        return "Song: " + title + " | Artist: " + artist + " | Album: " + album + " | Genre: " + genre.getName();
    }

    // -------- Getters --------

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "Song{title='" + title + "', artist='" + artist + "', album='" + album + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return Objects.equals(title, song.title) && Objects.equals(artist, song.artist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, artist);
    }
}
