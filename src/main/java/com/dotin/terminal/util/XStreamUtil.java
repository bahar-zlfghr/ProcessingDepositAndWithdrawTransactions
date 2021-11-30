package com.dotin.terminal.util;

import com.dotin.terminal.model.data.Response;
import com.dotin.terminal.model.data.ResponseList;
import com.thoughtworks.xstream.XStream;

/**
 * @author : Bahar Zolfaghari
 **/
public interface XStreamUtil {

    static XStream createXStream() {
        XStream xStream = new XStream();
        xStream.alias("responses", ResponseList.class);
        xStream.alias("response", Response.class);
        return xStream;
    }
}
