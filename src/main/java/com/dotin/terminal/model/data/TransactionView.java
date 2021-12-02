package com.dotin.terminal.model.data;

import com.dotin.terminal.validation.TransactionValidation;
import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Element;

import java.math.BigDecimal;

/**
 * @author : Bahar Zolfaghari
 **/
public class TransactionView {

    @Getter
    @Setter
    private String id;

    @Getter @Setter
    private TransactionType type;

    @Getter @Setter
    private BigDecimal amount;

    @Getter @Setter
    private String depositID;

    public TransactionView(String id, TransactionType type, BigDecimal amount, String depositID) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.depositID = depositID;
    }

    public static TransactionView createTransactionViewFromTransactionElement(Element transactionElement) {
        String id = transactionElement.getAttribute("id");
        TransactionValidation.validateTransactionID(id);
        TransactionType type = TransactionType.getTransactionType(transactionElement.getAttribute("type"));
        TransactionValidation.validateTransactionType(type);
        BigDecimal amount = new BigDecimal(transactionElement.getAttribute("amount"));
        TransactionValidation.validateTransactionAmount(amount);
        String depositID = transactionElement.getAttribute("deposit");
        return new TransactionView(id, type, amount, depositID);
    }
}
