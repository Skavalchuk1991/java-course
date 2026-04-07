package com.solvd.musicstreamingservice.model;

/**
 * Enum for notification types with priority and emoji.
 */
public enum NotificationType {
    INFO("Information", 1, "[i]"),
    WARNING("Warning", 2, "[!]"),
    PROMOTION("Promotion", 1, "[$]"),
    NEW_RELEASE("New Release", 3, "[*]"),
    SYSTEM("System Alert", 4, "[S]");

    private final String description;
    private final int priority;
    private final String icon;

    static {
        System.out.println("NotificationType enum loaded");
    }

    NotificationType(String description, int priority, String icon) {
        this.description = description;
        this.priority = priority;
        this.icon = icon;
    }

    public String getDescription() { return description; }
    public int getPriority() { return priority; }
    public String getIcon() { return icon; }

    public boolean isHighPriority() {
        return priority >= 3;
    }

    public String format(String message) {
        return icon + " " + description + ": " + message;
    }
}
