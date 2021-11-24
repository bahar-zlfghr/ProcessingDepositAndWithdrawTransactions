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
public abstract class DocumentUtil {
    private final static String ROOT = "src/main/resources/terminal/";

    public static Document createDocument(String fileName) throws ParserConfigurationException, IOException, SAXException {
        File file = new File(ROOT +  fileName);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(file);
    }
}
