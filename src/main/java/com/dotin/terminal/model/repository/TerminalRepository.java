package com.dotin.terminal.model.repository;

import com.dotin.terminal.model.data.*;
import com.dotin.terminal.util.DocumentUtil;
import lombok.Getter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author : Bahar Zolfaghari
 **/
public abstract class TerminalRepository {

    @Getter
    private static final List<Terminal> terminals = new ArrayList<>();

    static {
        fetchAllTerminals();
    }

    public static void fetchAllTerminals() {
        try {
            Set<Document> documents = DocumentUtil.getDocuments();
            documents.forEach(document -> {
                Node terminalNode = document.getElementsByTagName("terminal").item(0);
                Terminal terminal = fetchTerminal(terminalNode);

                Node serverNode = document.getElementsByTagName("server").item(0);
                Server server = fetchServer(serverNode);
                terminal.setServer(server);

                Node outLogNode = document.getElementsByTagName("outLog").item(0);
                String outLogPath = fetchLogFileName(outLogNode);
                terminal.setOutLogPath(outLogPath);

                NodeList transactionNodes = document.getElementsByTagName("transaction");
                for (int i = 0; i < transactionNodes.getLength(); i++) {
                    Node transactionNode = transactionNodes.item(i);
                    Transaction transaction = fetchTransaction(transactionNode);
                    terminal.getTransactions().add(transaction);
                }
                terminals.add(terminal);
            });
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private static Terminal fetchTerminal(Node terminalNode) {
        Terminal terminal = null;
        if (terminalNode.getNodeType() == Node.ELEMENT_NODE) {
            Element transactionElement = (Element) terminalNode;
            String id = transactionElement.getAttribute("id");
            //TODO: terminal type error
            TerminalType type = TerminalType.getTerminalType(transactionElement.getAttribute("type"));
            terminal = new Terminal(id, type);
        }
        return terminal;
    }

    private static Server fetchServer(Node serverNode) {
        Server server = null;
        if (serverNode.getNodeType() == Node.ELEMENT_NODE) {
            Element transactionElement = (Element) serverNode;
            String ip = transactionElement.getAttribute("ip");
            String port = transactionElement.getAttribute("port");
            server = new Server(ip, port);
        }
        return server;
    }

    private static String fetchLogFileName(Node outLogNode) {
        String outLog = "";
        if (outLogNode.getNodeType() == Node.ELEMENT_NODE) {
            Element transactionElement = (Element) outLogNode;
            outLog = transactionElement.getAttribute("path");
        }
        return outLog;
    }

    private static Transaction fetchTransaction(Node transactionNode) {
        Transaction transaction = null;
        if (transactionNode.getNodeType() == Node.ELEMENT_NODE) {
            Element transactionElement = (Element) transactionNode;
            String id = transactionElement.getAttribute("id");
            //TODO: transaction type error
            TransactionType type = TransactionType.getTransactionType(transactionElement.getAttribute("type"));
            //TODO: transaction amount error
            BigDecimal amount = new BigDecimal(transactionElement.getAttribute("amount"));
            String depositID = transactionElement.getAttribute("deposit");
            transaction = new Transaction(id, type, amount, depositID);
        }
        return transaction;
    }
}
