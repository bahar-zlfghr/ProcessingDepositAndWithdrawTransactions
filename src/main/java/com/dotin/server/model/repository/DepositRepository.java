package com.dotin.server.model.repository;

import com.dotin.server.model.data.Deposit;
import com.dotin.server.model.data.ServerLogFile;
import com.dotin.server.util.JsonUtil;
import lombok.Getter;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Bahar Zolfaghari
 **/
public class DepositRepository {

    @Getter
    private static final List<Deposit> deposits = new ArrayList<>();
    private static final Object lockObject = new Object();
    private static Logger logger;

    public static void fetchDeposits() {
        System.setProperty("LogFilePath", ServerLogFile.getLogFilePath());
        logger = Logger.getLogger(DepositRepository.class);
        try {
            JSONObject jsonObject = JsonUtil.getJsonObject();
            JSONArray depositsArray = jsonObject.getJSONArray("deposits");
            for (int i = 0; i < depositsArray.length(); i++) {
                String id = depositsArray.getJSONObject(i).getString("id");
                String customerFullName = depositsArray.getJSONObject(i).getString("customer");
                BigDecimal initialBalance = depositsArray.getJSONObject(i).getBigDecimal("initialBalance");
                BigDecimal upperBound = depositsArray.getJSONObject(i).getBigDecimal("upperBound");
                Deposit deposit = new Deposit(id, customerFullName, initialBalance, upperBound);
                deposits.add(deposit);
            }
        } catch (IOException ioException) {
            logger.error(ioException.getMessage(), ioException);
        }
        logger.info("Deposits info fetched successfully");
    }

    public static void UpdateDeposits() {
        logger.info("Deposits info updates if after one minute no terminals connect to the server");
        synchronized (lockObject) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("port", ServerRepository.getServer().getPort());
            jsonObject.put("deposits", deposits);
            jsonObject.put("outLog", ServerRepository.getServer().getOutLogPath());
            try {
                PrintWriter writer = new PrintWriter("src/main/resources/server/core");
                writer.write(String.valueOf(jsonObject)
                        .replace("{", "{\n  ")
                        .replace(",", ",\n  ")
                        .replace("[", "[\n ")
                );
                writer.flush();
                writer.close();
            } catch (IOException ioException) {
                logger.error(ioException.getMessage(), ioException);
            }
            logger.info("Deposits info updates successfully");
        }
    }
}
