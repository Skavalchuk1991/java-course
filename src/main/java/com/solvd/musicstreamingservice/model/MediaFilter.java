package com.solvd.musicstreamingservice.model;

/**
 * Custom functional interface: filters items by a condition.
 */
@FunctionalInterface
public interface MediaFilter<T> {
    boolean test(T item);
}
