package com.dotin.terminal.model.repository;

import com.dotin.terminal.model.data.Response;
import com.dotin.terminal.model.data.ResponseList;
import com.dotin.terminal.util.PrintWriterUtil;
import com.dotin.terminal.util.XStreamUtil;
import com.thoughtworks.xstream.XStream;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * @author : Bahar Zolfaghari
 **/
public class ResponseRepository {
    private static final XStream xStream = XStreamUtil.createXStream();

    public static void saveResponses(String fileName) throws FileNotFoundException {
        xStream.addImplicitCollection(ResponseList.class, "responses");
        List<Response> responses = ResponseList.getResponses();
        String xml = xStream.toXML(responses);
        PrintWriterUtil.writeXml(xml, "src/main/resources/" + fileName);
    }
}
