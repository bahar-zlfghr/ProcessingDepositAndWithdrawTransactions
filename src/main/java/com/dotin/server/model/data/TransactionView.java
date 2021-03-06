package com.dotin.server.model.data;

import lombok.Getter;

import java.math.BigDecimal;

/**
 * @author : Bahar Zolfaghari
 **/
public class TransactionView {

    @Getter
    private final TransactionType type;

    @Getter
    private final BigDecimal amount;

    @Getter
    private final String depositID;

    public TransactionView(TransactionType type, BigDecimal amount, String depositID) {
        this.type = type;
        this.amount = amount;
        this.depositID = depositID;
    }
}
