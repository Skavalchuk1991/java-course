package lesson2.music.model;

/**
 * Represents anything that can be reviewed by a user.
 */
public interface Reviewable {
    void addReview(Review review);
    double getAverageRating();
}
