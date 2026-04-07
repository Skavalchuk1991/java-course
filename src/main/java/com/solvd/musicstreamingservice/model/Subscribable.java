package com.solvd.musicstreamingservice.model;

/**
 * Represents anything that requires a subscription to access.
 */
public interface Subscribable {

    boolean hasAccess(User user);

    String getRequiredSubscriptionType();

}
