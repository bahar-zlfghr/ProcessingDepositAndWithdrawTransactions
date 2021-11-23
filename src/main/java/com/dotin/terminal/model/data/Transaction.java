package com.dotin.terminal.model.data;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author : Bahar Zolfaghari
 **/
public class Transaction {

    @Getter @Setter
    private String id;

    @Getter @Setter
    private TransactionType type;

    @Getter @Setter
    private BigDecimal amount;

    @Getter @Setter
    private String depositID;

    public Transaction(String id, TransactionType type, BigDecimal amount, String depositID) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.depositID = depositID;
    }
}
