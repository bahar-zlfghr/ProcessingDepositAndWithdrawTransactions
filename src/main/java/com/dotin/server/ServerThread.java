package com.dotin.server;

import com.dotin.server.model.data.Deposit;
import com.dotin.server.service.DepositService;

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

    public ServerThread(Socket connectionSocket) throws IOException {
        this.connectionSocket = connectionSocket;
        this.reader = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(connectionSocket.getOutputStream()));
    }

    @Override
    public void run() {
        while (true) {
            try {
                String data = reader.readLine();
                if (data.equalsIgnoreCase("end")) {
                    connectionSocket.close();
                    break;
                }
                else {
                    String[] tokens = data.split(", ");
                    String type = tokens[0];
                    BigDecimal amount = new BigDecimal(tokens[1]);
                    String depositID = tokens[2];
                    Optional<Deposit> depositByID = DepositService.getDepositByID(depositID);
                    if (depositByID.isPresent()) {
                        Deposit deposit = depositByID.get();
                        if (type.equalsIgnoreCase("deposit")) {
                            try {
                                DepositService.deposit(deposit, amount);
                                String response = String.format(RESPONSE_FORMAT, "done", TRANSACTION_DONE);
                                writer.write(response + "\n");
                                writer.flush();
                            } catch (Exception e) {
                                String response = String.format(RESPONSE_FORMAT, "failed", e.getMessage());
                                writer.write(response + "\n");
                                writer.flush();
                            }
                        }
                        else if (type.equalsIgnoreCase("withdraw")) {
                            try {
                                DepositService.withdraw(deposit, amount);
                                String response = String.format(RESPONSE_FORMAT, "done", TRANSACTION_DONE);
                                writer.write(response + "\n");
                                writer.flush();
                            } catch (Exception e) {
                                String response = String.format(RESPONSE_FORMAT, "failed", e.getMessage());
                                writer.write(response + "\n");
                                writer.flush();
                            }
                        }
                    }
                    else {
                        String message = "No deposit was found with " + depositID + " id!";
                        String response = String.format(RESPONSE_FORMAT, "failed", message);
                        writer.write(response + "\n");
                        writer.flush();
                    }
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
