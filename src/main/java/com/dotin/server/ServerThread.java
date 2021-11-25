package com.dotin.server;

import com.dotin.server.model.data.Deposit;
import com.dotin.server.model.data.ServerLogFile;
import com.dotin.server.service.DepositService;
import org.apache.log4j.Logger;

import java.io.*;
import java.math.BigDecimal;
import java.net.Socket;
import java.util.Optional;

/**
 * @author : Bahar Zolfaghari
 **/
public class ServerThread implements Runnable {
    private static final String TRANSACTION_DONE = "Transaction done.";
    private static final String RESPONSE_FORMAT = "%s, %s";
    private final Socket connectionSocket;
    private final BufferedReader reader;
    private final BufferedWriter writer;
    private static Logger logger;

    public ServerThread(Socket connectionSocket) throws IOException {
        System.setProperty("LogFilePath", ServerLogFile.getLogFilePath());
        logger = Logger.getLogger(ServerThread.class);
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
                    connectionSocket.close();
                    logger.info("Server closed terminal socket if it sends 'end' keyword");
                    break;
                }
                else {
                    String[] tokens = data.split(", ");
                    String type = tokens[0];
                    BigDecimal amount = new BigDecimal(tokens[1]);
                    String depositID = tokens[2];
                    Optional<Deposit> depositByID = DepositService.getDepositByID(depositID);
                    logger.info("The server check the deposit with id that terminal sends is exist or not");
                    if (depositByID.isPresent()) {
                        Deposit deposit = depositByID.get();
                        if (type.equalsIgnoreCase("deposit")) {
                            logger.info("Server starts doing deposit transaction...");
                            try {
                                DepositService.deposit(deposit, amount);
                                String response = String.format(RESPONSE_FORMAT, "done", TRANSACTION_DONE);
                                logger.info("Deposit transaction done successfully");
                                writer.write(response + "\n");
                                writer.flush();
                            } catch (Exception e) {
                                logger.error(e.getMessage(), e);
                                String response = String.format(RESPONSE_FORMAT, "failed", e.getMessage());
                                writer.write(response + "\n");
                                writer.flush();
                            }
                        }
                        else if (type.equalsIgnoreCase("withdraw")) {
                            logger.info("Server starts doing withdraw transaction...");
                            try {
                                DepositService.withdraw(deposit, amount);
                                String response = String.format(RESPONSE_FORMAT, "done", TRANSACTION_DONE);
                                logger.info("Withdraw transaction done successfully");
                                writer.write(response + "\n");
                                writer.flush();
                            } catch (Exception e) {
                                logger.error(e.getMessage(), e);
                                String response = String.format(RESPONSE_FORMAT, "failed", e.getMessage());
                                writer.write(response + "\n");
                                writer.flush();
                            }
                        }
                    }
                    else {
                        logger.info("The server not found no deposit with " + depositID + " id!");
                        String message = "No deposit was found with " + depositID + " id!";
                        String response = String.format(RESPONSE_FORMAT, "failed", message);
                        writer.write(response + "\n");
                        writer.flush();
                    }
                }
            } catch (IOException ioException) {
                logger.error(ioException.getMessage(), ioException);
            }
        }
    }
}
