package com.dotin.terminal.util;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author : Bahar Zolfaghari
 **/
public abstract class DocumentUtil {
    private final static String ROOT = "src/main/resources";

    public static Set<Document> getDocuments() throws IOException {
        Set<String> fileNames = getFileNames();
        return fileNames.stream().map(fileName -> {
            Document document = null;
            try {
                document = createDocument(fileName);
            } catch (ParserConfigurationException | IOException | SAXException e) {
                e.printStackTrace();
            }
            return document;
        }).collect(Collectors.toSet());
    }

    private static Set<String> getFileNames() throws IOException {
        try (Stream<Path> pathStream = Files.walk(Paths.get(ROOT + "/terminal"), 1)) {
            return pathStream
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toSet());
        }
    }

    private static Document createDocument(String fileName) throws ParserConfigurationException, IOException, SAXException {
        File file = new File(ROOT + "/terminal", fileName);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(file);
    }
}
