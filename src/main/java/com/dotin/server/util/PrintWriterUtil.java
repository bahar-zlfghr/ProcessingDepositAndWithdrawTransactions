package com.dotin.server.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    static void writeJson(JSONObject jsonObject, String path) throws FileNotFoundException, JsonProcessingException {
        try (PrintWriter writer = PrintWriterUtil.getPrintWriter(path)) {
            ObjectMapper objectMapper = new ObjectMapper();
            String prettyJsonObject = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
            writer.write(prettyJsonObject);
            writer.flush();
        }
    }
}
