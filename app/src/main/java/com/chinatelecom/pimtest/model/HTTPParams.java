package com.chinatelecom.pimtest.model;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Shuo on 2016/6/21.
 */
public class HTTPParams {
    private HTTPMethod method;
    private DataFormat format;
    private List<NameValuePair> valuePairs = new ArrayList<>();
    private String jsonParamStr;
    private byte[] binaryData;
	private Map<String, String> header = new HashMap<String, String>();

    public List<NameValuePair> getValuePairs() {
        return valuePairs;
    }

    public void setValuePairs(List<NameValuePair> valuePairs) {
        this.valuePairs = valuePairs;
    }


    public DataFormat getFormat() {
        return format;
    }

    public void setFormat(DataFormat format) {
        this.format = format;
    }

    public HTTPMethod getMethod() {
        return method;
    }

    public void setMethod(HTTPMethod method) {
        this.method = method;
    }

    public void addJsonParam(String jsonStr){
         this.jsonParamStr = jsonStr;
    }

    public void addJsonParam(JSONObject jsonObject){
        this.jsonParamStr = jsonObject.toString();
    }

    public String getJsonParam(){
        return jsonParamStr;
    }

    public void addRequestParam(NameValuePair nameValuePair){
        valuePairs.add(nameValuePair);
    }

    public HTTPParams(HTTPMethod method,DataFormat format){
        this.method = method;
        this.format = format;
    }
    
    public void setHeader(Map<String, String> header) {
        this.header = header;
    }


    public void putToHeader(String key, String value) {
    	if(header!=null && !header.containsKey(key)){
    		this.header.put(key, value);
    	}
    }
    
    public Map<String, String> getHeaders(){
    	return this.header;
    }
    
    public byte[] getBinaryData() {
		return binaryData;
	}

	public void setBinaryData(byte[] binaryData) {
		this.binaryData = binaryData;
	}
}
