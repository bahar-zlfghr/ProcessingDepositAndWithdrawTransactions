package com.dotin.terminal.util;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * @author : Bahar Zolfaghari
 **/
public interface PrintWriterUtil {

    static PrintWriter getPrintWriter(String path) throws FileNotFoundException {
        return new PrintWriter(path);
    }

    static void writeXml(String xml, String path) throws FileNotFoundException {
        try (PrintWriter writer = PrintWriterUtil.getPrintWriter(path)) {
            writer.write(xml);
            writer.flush();
        }
    }
}
