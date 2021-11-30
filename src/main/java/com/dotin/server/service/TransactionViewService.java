package com.dotin.server.service;

import com.dotin.server.model.data.TransactionType;
import com.dotin.server.model.data.TransactionView;

import java.math.BigDecimal;

/**
 * @author : Bahar Zolfaghari
 **/
public interface TransactionViewService {

    static TransactionView createTransactionView(String data) {
        String[] tokens = data.split(", ");
        TransactionType type = TransactionType.valueOf(tokens[0]);
        BigDecimal amount = new BigDecimal(tokens[1]);
        String depositID = tokens[2];
        return new TransactionView(type, amount, depositID);
    }
}
