package com.dotin.server.model.data;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author : Bahar Zolfaghari
 **/
public class Deposit {

    @Getter @Setter
    private String id;

    @Getter @Setter
    private String customer;

    @Getter @Setter
    private BigDecimal initialBalance;

    @Getter @Setter
    private BigDecimal upperBound;

    public Deposit(String id, String customer, BigDecimal initialBalance, BigDecimal upperBound) {
        this.id = id;
        this.customer = customer;
        this.initialBalance = initialBalance;
        this.upperBound = upperBound;
    }
}
