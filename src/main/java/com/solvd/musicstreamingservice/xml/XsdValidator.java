package com.solvd.musicstreamingservice.xml;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;

/**
 * Validates XML files against XSD schema.
 */
public class XsdValidator {

    private static final Logger LOGGER = LogManager.getLogger(XsdValidator.class);

    public static boolean validate(String xmlPath, String xsdPath) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdPath));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xmlPath)));
            LOGGER.info("XML validation PASSED: {} is valid against {}", xmlPath, xsdPath);
            return true;
        } catch (Exception e) {
            LOGGER.error("XML validation FAILED: {}", e.getMessage());
            return false;
        }
    }
}
