package com.dotin.terminal.model.repository;

import com.dotin.terminal.exception.TransactionAmountInvalidException;
import com.dotin.terminal.exception.TransactionTypeMismatchException;
import com.dotin.terminal.model.data.TerminalLogFile;
import com.dotin.terminal.model.data.Transaction;
import com.dotin.terminal.model.data.TransactionType;
import com.dotin.terminal.util.DocumentUtil;
import lombok.Getter;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Bahar Zolfaghari
 **/
public class TransactionRepository {

    @Getter
    private static final List<Transaction> transactions = new ArrayList<>();

    public static void fetchTransactions(String terminalFileName) {
        System.setProperty("LogFilePath", TerminalLogFile.getLogFilePath());
        Logger logger = Logger.getLogger(TransactionRepository.class);
        try {
            Document document = DocumentUtil.createDocument(terminalFileName);
            NodeList transactionNodes = document.getElementsByTagName("transaction");
            for (int i = 0; i < transactionNodes.getLength(); i++) {
                Node transactionNode = transactionNodes.item(i);
                if (transactionNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element transactionElement = (Element) transactionNode;
                    String id = transactionElement.getAttribute("id");
                    TransactionType type = TransactionType.getTransactionType(transactionElement.getAttribute("type"));
                    if (type == null) {
                        throw new TransactionTypeMismatchException("TransactionTypeMismatchException");
                    }
                    BigDecimal amount = new BigDecimal(transactionElement.getAttribute("amount"));
                    if (amount.compareTo(BigDecimal.ZERO) < 0) {
                        throw new TransactionAmountInvalidException("The transaction amount less than zero!");
                    }
                    String depositID = transactionElement.getAttribute("deposit");
                    Transaction transaction = new Transaction(id, type, amount, depositID);
                    transactions.add(transaction);
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException | TransactionTypeMismatchException | TransactionAmountInvalidException e) {
            logger.error(e.getMessage(), e);
        }
        logger.info("Transactions info fetched successfully");
    }
}
