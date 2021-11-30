package com.dotin.terminal.model.repository;

import com.dotin.terminal.model.data.TerminalLogFile;
import com.dotin.terminal.model.data.Transaction;
import com.dotin.terminal.model.data.TransactionView;
import com.dotin.terminal.util.DocumentUtil;
import com.dotin.terminal.util.LoggerUtil;
import lombok.Getter;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Bahar Zolfaghari
 **/
public abstract class TransactionRepository {

    @Getter
    private static final List<Transaction> transactions = new ArrayList<>();

    @Getter
    private static final List<Exception> exceptions = new ArrayList<>();

    public static void fetchTransactions(String terminalFileName) {
        Logger logger = LoggerUtil.getLogger(TransactionRepository.class, TerminalLogFile.getLogFilePath());
        try {
            Document document = DocumentUtil.createDocument(terminalFileName);
            NodeList transactionNodes = document.getElementsByTagName("transaction");
            for (int i = 0; i < transactionNodes.getLength(); i++) {
                Node transactionNode = transactionNodes.item(i);
                if (transactionNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element transactionElement = (Element) transactionNode;
                    TransactionView transactionView = TransactionView.createTransactionViewFromTransactionElement(transactionElement);
                    Transaction transaction = new Transaction(transactionView);
                    if (exceptions.isEmpty()) {
                        transactions.add(transaction);
                    }
                    else {
                        LoggerUtil.exceptionsLog(exceptions);
                    }
                    exceptions.clear();
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            logger.error(e.getMessage(), e);
        }
        logger.info("Transactions info fetched successfully");
    }
}
