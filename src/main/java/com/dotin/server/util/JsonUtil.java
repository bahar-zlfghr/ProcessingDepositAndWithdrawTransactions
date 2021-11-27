package com.dotin.server.util;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author : Bahar Zolfaghari
 **/
public interface JsonUtil {

    static JSONObject getJsonObject(String fileName) throws IOException {
        String jsonFileContent = new String(Files.readAllBytes(Paths.get("src/main/resources/server/" + fileName)));
        return new JSONObject(jsonFileContent);
    }

    static JSONObject getJsonObject() {
        return new JSONObject();
    }
}
