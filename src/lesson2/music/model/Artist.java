package lesson2.music.model;

/**
 * Represents a music artist in the system.
 * This is an independent domain entity (not inherited from Media).
 */
public class Artist {

    // Artist stage or real name
    private String name;

    // Country of origin
    private String country;

    // Year when artist started career
    private int debutYear;

    // Artist's albums
    private Album[] albums;

    /**
     * Constructor to initialize artist fields
     */
    public Artist(String name, String country, int debutYear) {
        this.name = name;
        this.country = country;
        this.debutYear = debutYear;
        this.albums = new Album[0];
    }

    // ----------- Getters -----------

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public int getDebutYear() {
        return debutYear;
    }

    public Album[] getAlbums() {
        return albums;
    }

    // ----------- Setters -----------

    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setDebutYear(int debutYear) {
        this.debutYear = debutYear;
    }

    public void setAlbums(Album[] albums) {
        this.albums = albums;
    }

    /**
     * Business method – prints short info about artist
     */
    public void printArtistInfo() {
        System.out.println(name + " from " + country + " (since " + debutYear + ")");
    }
}
