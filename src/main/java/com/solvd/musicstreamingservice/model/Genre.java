package com.solvd.musicstreamingservice.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

/**
 * Represents a music genre in the system.
 * Independent domain entity.
 */
public class Genre {

    private static final Logger LOGGER = LogManager.getLogger(Genre.class);

    // Name of the genre (e.g., Rock, Jazz, Hip-Hop)
    private String name;

    // Description of the genre
    private String description;

    /**
     * Constructor to initialize genre fields
     */
    public Genre(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // -------- Getters --------

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    // -------- Setters --------

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Business method – prints genre info
     */
    public void printGenreInfo() {
        LOGGER.info("Genre: {}", name);
        LOGGER.info("Description: {}", description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genre genre = (Genre) o;
        return Objects.equals(name, genre.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}