package com.dotin.server.service;

import com.dotin.server.exception.DepositBalanceNotEnoughException;
import com.dotin.server.exception.IncorrectTransactionAmountException;
import com.dotin.server.model.data.Deposit;
import com.dotin.server.model.repository.DepositRepository;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * @author : Bahar Zolfaghari
 **/
public abstract class DepositService {
    private static final Object lockObject = new Object();

    public static Optional<Deposit> getDepositByID(String depositID) {
        return DepositRepository.getDeposits().stream().filter(
                deposit -> deposit.getId().equals(depositID)).findFirst();
    }

    public static void deposit(Deposit deposit, BigDecimal amount) throws IncorrectTransactionAmountException {
        BigDecimal initialBalance = deposit.getInitialBalance();
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IncorrectTransactionAmountException("The transaction amount equal or less than zero!");
        }
        synchronized (lockObject) {
            BigDecimal newBalance = initialBalance.add(amount);
            deposit.setInitialBalance(newBalance);
        }
    }

    public static void withdraw(Deposit deposit, BigDecimal amount) throws IncorrectTransactionAmountException, DepositBalanceNotEnoughException {
        BigDecimal initialBalance = deposit.getInitialBalance();
        BigDecimal upperBound = deposit.getUpperBound();
        if (amount.compareTo(upperBound) > 0) {
            throw new IncorrectTransactionAmountException("The transaction amount is more than the deposit upper bound!");
        }
        if (initialBalance.compareTo(amount) < 0) {
            throw new DepositBalanceNotEnoughException("The deposit balance is less than transaction amount!");
        }
        synchronized (lockObject) {
            BigDecimal newBalance = initialBalance.subtract(amount);
            deposit.setInitialBalance(newBalance);
        }
    }

    public static Boolean depositValidation(String depositID) {
        return getDepositByID(depositID).isPresent();
    }
}
