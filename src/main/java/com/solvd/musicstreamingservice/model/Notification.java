package com.solvd.musicstreamingservice.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents a notification sent to a user.
 * Independent domain entity.
 */
public class Notification {

    private static final Logger LOGGER = LogManager.getLogger(Notification.class);

    // Recipient of the notification
    private User user;

    // Notification message
    private String message;

    // Type of notification (e.g., INFO, WARNING, PROMOTION)
    private String type;

    /**
     * Constructor to initialize notification fields
     */
    public Notification(User user, String message, String type) {
        this.user = user;
        this.message = message;
        this.type = type;
    }

    // -------- Getters --------

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    // -------- Setters --------

    public void setUser(User user) {
        this.user = user;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * Business method – simulate sending notification
     */
    public void send() {
        LOGGER.info("Notification to {}", user.getUsername());
        LOGGER.info("Type: {}", type);
        LOGGER.info("Message: {}", message);
    }
}