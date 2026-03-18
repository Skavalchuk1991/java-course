package lesson2.music.model;

/**
 * Podcast is another type of Media
 */
public class Podcast extends Media {

    // Host of the podcast
    private String host;

    // Episode number
    private int episodeNumber;

    /**
     * Constructor for Podcast
     */
    public Podcast(int id, String title, int duration, String host, int episodeNumber) {
        super(id, title, duration);
        this.host = host;
        this.episodeNumber = episodeNumber;
    }

    @Override
    public String getMediaInfo() {
        return "Podcast: " + title + " | Host: " + host + " | Episode: " + episodeNumber;
    }

    @Override
    public String toString() {
        return "Podcast{title='" + title + "', host='" + host + "', episode=" + episodeNumber + "}";
    }

    // -------- Getters --------

    public String getHost() {
        return host;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    // -------- Setters ---------

    public void setHost(String host) {
        this.host = host;
    }

    public void setEpisodeNumber(int episodeNumber) {
        this.episodeNumber = episodeNumber;
    }
}
