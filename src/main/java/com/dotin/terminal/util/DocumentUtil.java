package com.dotin.terminal.util;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * @author : Bahar Zolfaghari
 **/
public interface DocumentUtil {

    static Document createDocument(String fileName) throws ParserConfigurationException, IOException, SAXException {
        File file = new File("src/main/resources/terminal/" + fileName + ".xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(file);
    }
}
