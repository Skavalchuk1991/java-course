package com.solvd.musicstreamingservice.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utility class: reads a text file, counts unique words using StringUtils and FileUtils.
 */
public class WordCounter {

    private static final Logger LOGGER = LogManager.getLogger(WordCounter.class);

    public static void countUniqueWords(String inputPath, String outputPath) {
        try {
            // Read entire file content using FileUtils
            String content = FileUtils.readFileToString(new File(inputPath), StandardCharsets.UTF_8);

            // Normalize: lowercase, remove non-letter characters except spaces
            String cleaned = StringUtils.lowerCase(content);
            cleaned = cleaned.replaceAll("[^a-zA-Z\\s]", "");

            // Split into words, filter blanks, collect unique
            Set<String> uniqueWords = Arrays.stream(StringUtils.split(cleaned))
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.toSet());

            // Build result string
            String result = "Total unique words: " + uniqueWords.size() + "\n"
                    + StringUtils.join(uniqueWords, "\n");

            // Write result to file using FileUtils
            FileUtils.writeStringToFile(new File(outputPath), result, StandardCharsets.UTF_8);

            LOGGER.info("Unique words counted: {}", uniqueWords.size());
            LOGGER.info("Result written to: {}", outputPath);

        } catch (IOException e) {
            LOGGER.error("Error processing file: {}", e.getMessage());
        }
    }
}
