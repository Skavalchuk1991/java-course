package com.solvd.musicstreamingservice.xml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.solvd.musicstreamingservice.xml.model.XmlMusicService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * Jackson parser implementation — parses JSON files.
 */
public class JacksonParser implements Parser {

    private static final Logger LOGGER = LogManager.getLogger(JacksonParser.class);

    @Override
    public XmlMusicService parse(String filePath) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            XmlMusicService service = mapper.readValue(new File(filePath), XmlMusicService.class);
            LOGGER.info("Jackson parsing completed: {}", service);
            return service;
        } catch (Exception e) {
            LOGGER.error("Jackson parsing error: {}", e.getMessage());
            return new XmlMusicService();
        }
    }
}
