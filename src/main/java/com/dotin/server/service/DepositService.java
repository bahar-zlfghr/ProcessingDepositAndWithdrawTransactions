package com.dotin.server.service;

import com.dotin.server.model.data.Deposit;
import com.dotin.server.model.repository.DepositRepository;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * @author : Bahar Zolfaghari
 **/
public class DepositService {
    private static final Object lockObject = new Object();

    public static Optional<Deposit> getDepositByID(String depositID) {
        return DepositRepository.getDeposits().stream().filter(
                deposit -> deposit.getId().equals(depositID)).findFirst();
    }

    public static void deposit(Deposit deposit, BigDecimal amount) throws Exception {
        BigDecimal initialBalance = deposit.getInitialBalance();
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            synchronized (lockObject) {
                BigDecimal newBalance = initialBalance.add(amount);
                deposit.setInitialBalance(newBalance);
            }
        } else {
            throw new Exception("Deposit balance not enough for this transaction!");
        }
    }

    public static void withdraw(Deposit deposit, BigDecimal amount) throws Exception {
        BigDecimal initialBalance = deposit.getInitialBalance();
        BigDecimal upperBound = deposit.getUpperBound();
        if (amount.compareTo(upperBound) <= 0) {
            if (initialBalance.compareTo(amount) >= 0) {
                synchronized (lockObject) {
                    BigDecimal newBalance = initialBalance.subtract(amount);
                    deposit.setInitialBalance(newBalance);
                }
            } else {
                throw new Exception("The deposit balance not enough for this transaction!");
            }
        } else {
            throw new Exception("The transaction amount is more than the deposit withdraw limit!");
        }
    }
}
