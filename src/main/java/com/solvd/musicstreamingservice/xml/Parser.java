package com.solvd.musicstreamingservice.xml;

import com.solvd.musicstreamingservice.xml.model.XmlMusicService;

/**
 * Common interface for all parsers.
 */
public interface Parser {
    XmlMusicService parse(String filePath);
}
