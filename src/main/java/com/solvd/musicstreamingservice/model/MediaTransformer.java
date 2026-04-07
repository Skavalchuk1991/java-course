package com.solvd.musicstreamingservice.model;

/**
 * Custom functional interface: transforms one type to another.
 */
@FunctionalInterface
public interface MediaTransformer<T, R> {
    R transform(T input);
}
