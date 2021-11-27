package com.dotin.server.model.repository;

import com.dotin.server.ServerMain;
import com.dotin.server.model.data.Deposit;
import com.dotin.server.model.data.ServerLogFile;
import com.dotin.server.util.JsonUtil;
import com.dotin.server.util.LoggerUtil;
import com.dotin.server.util.PrintWriterUtil;
import lombok.Getter;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Bahar Zolfaghari
 **/
public class DepositRepository {

    @Getter
    private static final List<Deposit> deposits = new ArrayList<>();
    private static Logger logger;

    public static void fetchDeposits() {
        logger = LoggerUtil.getLogger(ServerMain.class, ServerLogFile.getLogFilePath());
        try {
            JSONObject jsonObject = JsonUtil.getJsonObject("core");
            JSONArray depositsArray = jsonObject.getJSONArray("deposits");
            for (int i = 0; i < depositsArray.length(); i++) {
                Deposit deposit = createDepositFromJsonObject(depositsArray.getJSONObject(i));
                deposits.add(deposit);
            }
            logger.info("Deposits info fetched successfully");
        } catch (IOException ioException) {
            logger.error(ioException.getMessage(), ioException);
        }
    }

    private static Deposit createDepositFromJsonObject(JSONObject jsonObject) {
        String id = jsonObject.getString("id");
        String customerFullName = jsonObject.getString("customer");
        BigDecimal initialBalance = jsonObject.getBigDecimal("initialBalance");
        BigDecimal upperBound = jsonObject.getBigDecimal("upperBound");
        return new Deposit(id, customerFullName, initialBalance, upperBound);
    }

    public static void saveDepositsChanges() {
        logger.info("The changes of deposits save if after one minute no terminals connect to the server");
        JSONObject jsonObject = JsonUtil.getJsonObject();
        jsonObject.put("port", ServerRepository.getServer().getPort());
        jsonObject.put("deposits", deposits);
        jsonObject.put("outLog", ServerRepository.getServer().getOutLogPath());
        writeJsonInFile(jsonObject);
    }

    private synchronized static void writeJsonInFile(JSONObject jsonObject) {
        try {
            PrintWriterUtil.writeJson(jsonObject, "src/main/resources/server/core");
            logger.info("The changes of deposits saved successfully");
        } catch (IOException ioException) {
            logger.error(ioException.getMessage(), ioException);
        }
    }
}
