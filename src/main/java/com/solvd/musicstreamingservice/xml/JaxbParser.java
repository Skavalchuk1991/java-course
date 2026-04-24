package com.solvd.musicstreamingservice.xml;

import com.solvd.musicstreamingservice.xml.model.XmlMusicService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;

/**
 * JAXB parser implementation — uses annotations to map XML to Java objects.
 */
public class JaxbParser implements Parser {

    private static final Logger LOGGER = LogManager.getLogger(JaxbParser.class);

    @Override
    public XmlMusicService parse(String filePath) {
        try {
            JAXBContext context = JAXBContext.newInstance(XmlMusicService.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            XmlMusicService service = (XmlMusicService) unmarshaller.unmarshal(new File(filePath));
            LOGGER.info("JAXB parsing completed: {}", service);
            return service;
        } catch (Exception e) {
            LOGGER.error("JAXB parsing error: {}", e.getMessage());
            return new XmlMusicService();
        }
    }
}
