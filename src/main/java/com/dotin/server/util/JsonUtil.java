package com.dotin.server.util;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author : Bahar Zolfaghari
 **/
public abstract class JsonUtil {
    private final static String ROOT = "src/main/resources";

    public static JSONObject getJsonObject() throws IOException {
        String jsonFileContent = new String(Files.readAllBytes(Paths.get(ROOT + "/server/core")));
        return new JSONObject(jsonFileContent);
    }
}
