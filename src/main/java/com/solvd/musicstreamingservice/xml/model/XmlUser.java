package com.solvd.musicstreamingservice.xml.model;

import jakarta.xml.bind.annotation.*;
import java.time.LocalDateTime;

@XmlAccessorType(XmlAccessType.FIELD)
public class XmlUser {

    @XmlAttribute
    private int id;

    private String username;
    private String email;
    private String subscriptionType;
    private boolean premium;
    private int downloadLimit;
    private LocalDateTime registeredAt;

    public XmlUser() {}

    public XmlUser(int id, String username, String email, String subscriptionType, boolean premium, int downloadLimit, LocalDateTime registeredAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.subscriptionType = subscriptionType;
        this.premium = premium;
        this.downloadLimit = downloadLimit;
        this.registeredAt = registeredAt;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSubscriptionType() { return subscriptionType; }
    public void setSubscriptionType(String subscriptionType) { this.subscriptionType = subscriptionType; }
    public boolean isPremium() { return premium; }
    public void setPremium(boolean premium) { this.premium = premium; }
    public int getDownloadLimit() { return downloadLimit; }
    public void setDownloadLimit(int downloadLimit) { this.downloadLimit = downloadLimit; }
    public LocalDateTime getRegisteredAt() { return registeredAt; }
    public void setRegisteredAt(LocalDateTime registeredAt) { this.registeredAt = registeredAt; }

    @Override
    public String toString() {
        return "XmlUser{id=" + id + ", username='" + username + "', premium=" + premium + "}";
    }
}
