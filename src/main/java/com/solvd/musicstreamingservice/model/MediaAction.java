package com.solvd.musicstreamingservice.model;

/**
 * Custom functional interface: performs an action on an item (no return).
 */
@FunctionalInterface
public interface MediaAction<T> {
    void execute(T item);
}
