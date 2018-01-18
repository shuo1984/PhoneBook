package com.chinatelecom.pimtest.net;

import java.util.HashMap;
import java.util.Map;

/**
 * @author owensun
 * @since 23/02/2012
 */
public class Packet {
    public enum MethodType {
        GET, POST
    }

    protected MethodType methodType;
    protected String url;
    protected Map<String, String> header = new HashMap<String, String>();

    public Packet(MethodType methodType, String url) {
        this.methodType = methodType;
        this.url = url;
    }

    public MethodType getMethodType() {
        return methodType;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

}
