package com.dotin.server.util;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * @author : Bahar Zolfaghari
 **/
public interface PrintWriterUtil {

    static PrintWriter getPrintWriter(String path) throws FileNotFoundException {
        return new PrintWriter(path);
    }

    static void writeJson(JSONObject jsonObject, String path) throws FileNotFoundException {
        try (PrintWriter writer = PrintWriterUtil.getPrintWriter(path)) {
            writer.write(
                    String.valueOf(jsonObject)
                    .replace("{", "{\n  ")
                    .replace(",", ",\n  ")
                    .replace("[", "[\n  "));
            writer.flush();
        }
    }
}
