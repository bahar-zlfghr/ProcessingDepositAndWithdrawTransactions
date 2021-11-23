package com.dotin.server.model.repository;

import com.dotin.server.model.data.Deposit;
import com.dotin.server.model.data.Server;
import com.dotin.server.util.JsonUtil;
import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author : Bahar Zolfaghari
 **/
public abstract class ServerRepository {

    @Getter
    private static final Server server = new Server();

    static {
        fetchServer();
    }

    private static void fetchServer() {
        try {
            JSONObject jsonObject = JsonUtil.getJsonObject();
            Integer port = jsonObject.getInt("port");
            server.setPort(port);
            JSONArray depositsArray = jsonObject.getJSONArray("deposits");
            for (int i = 0; i < depositsArray.length(); i++) {
                String id = depositsArray.getJSONObject(i).getString("id");
                String customerFullName = depositsArray.getJSONObject(i).getString("customer");
                BigDecimal initialBalance = depositsArray.getJSONObject(i).getBigDecimal("initialBalance");
                BigDecimal upperBound = depositsArray.getJSONObject(i).getBigDecimal("upperBound");
                Deposit deposit = new Deposit(id, customerFullName, initialBalance, upperBound);
                server.getDeposits().add(deposit);
            }
            String outLogPath = jsonObject.getString("outLog");
            server.setOutLogPath(outLogPath);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
