package com.dotin.terminal.model.repository;

import com.dotin.terminal.model.data.*;
import com.dotin.terminal.util.DocumentUtil;
import lombok.Getter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * @author : Bahar Zolfaghari
 **/
public class TerminalRepository {

    @Getter
    private static final Terminal terminal = new Terminal();

    public static void fetchTerminal(String terminalFileName) {
        try {
            Document document = DocumentUtil.createDocument(terminalFileName);
            Node terminalNode = document.getElementsByTagName("terminal").item(0);
            setTerminalInfo(terminalNode);
            Node serverNode = document.getElementsByTagName("server").item(0);
            setServerInfo(serverNode);
            Node outLogNode = document.getElementsByTagName("outLog").item(0);
            setLogFileNameInfo(outLogNode);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void setTerminalInfo(Node terminalNode) throws Exception {
        if (terminalNode.getNodeType() == Node.ELEMENT_NODE) {
            Element transactionElement = (Element) terminalNode;
            String id = transactionElement.getAttribute("id");
            TerminalType type = TerminalType.getTerminalType(transactionElement.getAttribute("type"));
            if (type == null) {
                throw new Exception("TerminalTypeMismatchException");
            }
            else {
                terminal.setId(id);
                terminal.setType(type);
            }
        }
    }

    private static void setServerInfo(Node serverNode) {
        if (serverNode.getNodeType() == Node.ELEMENT_NODE) {
            Element transactionElement = (Element) serverNode;
            String serverIP = transactionElement.getAttribute("ip");
            Integer serverPort = Integer.valueOf(transactionElement.getAttribute("port"));
            terminal.setServerIP(serverIP);
            terminal.setServerPort(serverPort);
        }
    }

    private static void setLogFileNameInfo(Node outLogNode) {
        if (outLogNode.getNodeType() == Node.ELEMENT_NODE) {
            Element transactionElement = (Element) outLogNode;
            String outLogPath = transactionElement.getAttribute("path");
            terminal.setOutLogPath(outLogPath);
        }
    }
}
