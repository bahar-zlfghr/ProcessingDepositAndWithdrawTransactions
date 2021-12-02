package com.dotin.server.validation;

import com.dotin.server.exception.DepositBalanceNotEnoughException;
import com.dotin.server.exception.IncorrectTransactionAmountException;

import java.math.BigDecimal;

/**
 * @author : Bahar Zolfaghari
 **/
public interface TransactionValidation {

    static void validateWithdrawal(BigDecimal amount, BigDecimal upperBound, BigDecimal initialBalance) throws IncorrectTransactionAmountException, DepositBalanceNotEnoughException {
        if (amount.compareTo(upperBound) > 0) {
            throw new IncorrectTransactionAmountException("The transaction amount is more than the deposit upper bound!");
        }
        if (initialBalance.compareTo(amount) < 0) {
            throw new DepositBalanceNotEnoughException("The deposit balance is less than transaction amount!");
        }
    }

    static void validateDeposit(BigDecimal amount) throws IncorrectTransactionAmountException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IncorrectTransactionAmountException("The transaction amount equal or less than zero!");
        }
    }
}
