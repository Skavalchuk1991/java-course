package lesson2.music.model;

/**
 * Represents user's personal media library.
 * Independent domain entity.
 */
public class Library {

    // Owner of the library
    private User owner;

    // Stored media in library
    private Media[] mediaItems;

    /**
     * Constructor initializes library with empty media array
     */
    public Library(User owner) {
        this.owner = owner;
        this.mediaItems = new Media[0]; // start with empty library
    }

    // -------- Getters --------

    public User getOwner() {
        return owner;
    }

    public Media[] getMediaItems() {
        return mediaItems;
    }

    // -------- Business Methods --------

    /**
     * Adds media to library.
     * Creates a new array and copies existing elements.
     */
    public void addMedia(Media media) {

        Media[] newMedia = new Media[mediaItems.length + 1];

        System.arraycopy(mediaItems, 0, newMedia, 0, mediaItems.length);

        newMedia[mediaItems.length] = media;

        mediaItems = newMedia;
    }

    /**
     * Prints library content
     */
    public void printLibrary() {

        System.out.println("Library of user: " + owner.getUsername());

        for (Media media : mediaItems) {
            System.out.println("- " + media.getTitle());
        }
    }

    // ---------- Setters ----------

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setMediaItems(Media[] mediaItems) {
        this.mediaItems = mediaItems;
    }
}