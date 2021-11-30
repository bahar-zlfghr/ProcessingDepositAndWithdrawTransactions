package com.dotin.server;

import com.dotin.server.exception.DepositBalanceNotEnoughException;
import com.dotin.server.exception.IncorrectTransactionAmountException;
import com.dotin.server.model.data.Deposit;
import com.dotin.server.model.data.ServerLogFile;
import com.dotin.server.model.data.TransactionView;
import com.dotin.server.service.DepositService;
import com.dotin.server.service.TransactionViewService;
import com.dotin.server.util.LoggerUtil;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;

/**
 * @author : Bahar Zolfaghari
 **/
public class ServerThread implements Runnable {
    private static final String RESPONSE_FORMAT = "%s, %s";
    private final Socket connectionSocket;
    private final BufferedReader reader;
    private final BufferedWriter writer;
    private static Logger logger;

    public ServerThread(Socket connectionSocket) throws IOException {
        logger = LoggerUtil.getLogger(ServerMain.class, ServerLogFile.getLogFilePath());
        this.connectionSocket = connectionSocket;
        this.reader = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(connectionSocket.getOutputStream()));
    }

    @Override
    public void run() {
        while (true) {
            try {
                String data = reader.readLine();
                logger.info("Server receive the data that terminal sends");
                if (data.equalsIgnoreCase("end")) {
                    endTask();
                    break;
                } else {
                    TransactionView transactionView = TransactionViewService.createTransactionView(data);
                    logger.info("The server check the deposit with id that terminal sends is exist or not");
                    if (Boolean.FALSE.equals(DepositService.depositValidation(transactionView.getDepositID()))) {
                        incorrectDepositIDTask(transactionView);
                    }
                    else {
                        Deposit deposit = DepositService.getDepositByID(transactionView.getDepositID()).orElse(null);
                        doTransaction(deposit, transactionView);
                    }
                }
            } catch (IOException ioException) {
                logger.error(ioException.getMessage(), ioException);
            }
        }
    }

    private void endTask() throws IOException {
        reader.close();
        writer.close();
        connectionSocket.close();
        logger.info("Server closed terminal socket & resources if it sends 'end' keyword");
    }

    private void incorrectDepositIDTask(TransactionView transactionView) throws IOException {
        logger.info("The server not found no deposit with " + transactionView.getDepositID() + " id!");
        String message = "No deposit was found with " + transactionView.getDepositID() + " id!";
        String response = String.format(RESPONSE_FORMAT, "failed", message);
        sendResponseToTerminal(response);
    }

    private void doTransaction(Deposit deposit, TransactionView transactionView) throws IOException {
        switch (transactionView.getType()) {
            case DEPOSIT:
                depositTransactionTask(transactionView, deposit);
                break;
            case WITHDRAW:
                withdrawTransactionTask(transactionView, deposit);
                break;
        }
    }

    private void depositTransactionTask(TransactionView transactionView, Deposit deposit) throws IOException {
        logger.info("Server starts doing deposit transaction...");
        String response;
        try {
            DepositService.deposit(deposit, transactionView.getAmount());
            response = String.format(RESPONSE_FORMAT, "done", "Transaction done");
            logger.info("Deposit transaction done successfully");
        } catch (IncorrectTransactionAmountException e) {
            response = String.format(RESPONSE_FORMAT, "failed", e.getMessage());
            logger.error(e.getMessage(), e);
        }
        sendResponseToTerminal(response);
    }

    private void withdrawTransactionTask(TransactionView transactionView, Deposit deposit) throws IOException {
        logger.info("Server starts doing withdraw transaction...");
        String response;
        try {
            DepositService.withdraw(deposit, transactionView.getAmount());
            response = String.format(RESPONSE_FORMAT, "done", "Transaction done");
            logger.info("Withdraw transaction done successfully");
        } catch (DepositBalanceNotEnoughException | IncorrectTransactionAmountException e) {
            response = String.format(RESPONSE_FORMAT, "failed", e.getMessage());
            logger.error(e.getMessage(), e);
        }
        sendResponseToTerminal(response);
    }

    private void sendResponseToTerminal(String response) throws IOException {
        writer.write(response + "\n");
        writer.flush();
    }
}
