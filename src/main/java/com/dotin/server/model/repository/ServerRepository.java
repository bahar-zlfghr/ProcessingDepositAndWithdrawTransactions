package com.dotin.server.model.repository;

import com.dotin.server.model.data.Server;
import com.dotin.server.util.JsonUtil;
import lombok.Getter;
import org.json.JSONObject;

import java.io.IOException;

/**
 * @author : Bahar Zolfaghari
 **/
public abstract class ServerRepository {

    @Getter
    private static final Server server = new Server();

    public static void fetchServer() {
        try {
            JSONObject jsonObject = JsonUtil.getJsonObject();
            Integer port = jsonObject.getInt("port");
            server.setPort(port);
            String outLogPath = jsonObject.getString("outLog");
            server.setOutLogPath(outLogPath);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
