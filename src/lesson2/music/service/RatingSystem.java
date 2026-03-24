package lesson2.music.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic rating system that can rate any type of item.
 * T is the type of item being rated (e.g. Song, Podcast).
 */
public class RatingSystem<T> {

    private List<T> items = new ArrayList<>();
    private List<Integer> ratings = new ArrayList<>();

    public void addRating(T item, int rating) {
        items.add(item);
        ratings.add(rating);
    }

    public double getAverageRating() {
        if (ratings.isEmpty()) return 0.0;
        int total = 0;
        for (int r : ratings) {
            total += r;
        }
        return (double) total / ratings.size();
    }

    public List<T> getItems() {
        return items;
    }

    public List<Integer> getRatings() {
        return ratings;
    }

    public int size() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
