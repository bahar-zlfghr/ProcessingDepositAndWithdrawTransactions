package com.dotin.terminal.validation;

import com.dotin.terminal.exception.DuplicateTransactionIDException;
import com.dotin.terminal.exception.TransactionAmountInvalidException;
import com.dotin.terminal.exception.TransactionTypeMismatchException;
import com.dotin.terminal.model.data.TransactionType;
import com.dotin.terminal.model.repository.TransactionRepository;

import java.math.BigDecimal;

/**
 * @author : Bahar Zolfaghari
 **/
public interface TransactionValidation {

    static void validateTransactionID(String id) {
        if (TransactionRepository.getTransactions()
                .stream().anyMatch(transaction -> transaction.getId().equals(id))) {
            TransactionRepository.getExceptions().add(
                    new DuplicateTransactionIDException("The transaction id is duplicated!"));
        }
    }

    static void validateTransactionType(TransactionType type) {
        if (type == null) {
            TransactionRepository.getExceptions().add(new TransactionTypeMismatchException("Transaction type mismatch!"));
        }
    }

    static void validateTransactionAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            TransactionRepository.getExceptions().add(new TransactionAmountInvalidException("The transaction amount less than zero!"));
        }
    }
}
