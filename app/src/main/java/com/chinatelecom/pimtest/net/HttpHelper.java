package com.chinatelecom.pimtest.net;

import com.chinatelecom.pimtest.interfaces.Closure;
import com.chinatelecom.pimtest.model.DataFormat;
import com.chinatelecom.pimtest.model.HTTPMethod;
import com.chinatelecom.pimtest.model.HTTPParams;
import com.chinatelecom.pimtest.utils.StringUtils;

import org.apache.http.NameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;




/**
 * Created by Shuo on 2016/6/21.
 */
public class HttpHelper {

    private static final String DefaultEncoding = "UTF-8";

    public static void sendHttpRequest(String api, HTTPParams params, String encoding, Closure closure){
        HttpURLConnection connection = null;
        
        try {
            if(params.getMethod() == HTTPMethod.GET){
            	
                if (params.getValuePairs() != null && params.getValuePairs().size()>0) {
                    api += "?";
                    for(NameValuePair pair : params.getValuePairs()){
                        if(api.endsWith("?")) {
                            api += URLEncoder.encode(pair.getName() + "=" + pair.getValue(),encoding) ;
                        }else{
                            api += "&" + URLEncoder.encode(pair.getName() + "=" + pair.getValue(),encoding);
                        }
                    }
                    api = api.replaceAll("%0A", "");
                }
                URL url = new URL(api);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setUseCaches(false);
                connection.setInstanceFollowRedirects(true);
                connection.setRequestProperty("encoding",encoding);
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(6000);
                connection.setReadTimeout(36000);
                for (String k : params.getHeaders().keySet()) {
                    connection.addRequestProperty(k, params.getHeaders().get(k));
                }
                
            }else if(params.getMethod() == HTTPMethod.POST) {
                URL url = new URL(api);
               
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setUseCaches(false);
                connection.setInstanceFollowRedirects(true);
                connection.setRequestProperty("encoding", encoding);
                connection.setConnectTimeout(6000);
                connection.setReadTimeout(36000);
                connection.setRequestMethod("POST");
                if(params.getFormat() == DataFormat.JSON) {
                    connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                    connection.setRequestProperty("Accept", "application/json");
                }else if(params.getFormat() == DataFormat.Binary){
                	connection.setRequestProperty("Content-Type", "application/octet-stream");   
                	connection.setRequestProperty("Connection", "Keep-Alive");// 维持长连接   
                	connection.setRequestProperty("Charset", "UTF-8");   
                }else{
                    connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                }
                for (String k : params.getHeaders().keySet()) {
                    connection.addRequestProperty(k, params.getHeaders().get(k));
                }
                connection.connect();
                //POST请求
                String postStr = StringUtils.EMPTY;
                if(params.getFormat() == DataFormat.JSON){
                	postStr = params.getJsonParam();
                }
                System.out.println("###### post json: " + params.getJsonParam());
                OutputStream os = connection.getOutputStream();
              
                if(params.getFormat() == DataFormat.Binary){
                	 os.write(params.getBinaryData());
                	 os.flush();
                	 os.close();
                }else{
                	 OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
                	 osw.write(postStr);
                	 osw.flush();
                     osw.close();
                }
               
            }
            //读取响应
            if(connection.getResponseCode()==200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        connection.getInputStream(),"UTF-8"));
                String lines;
                StringBuffer sb = new StringBuffer("");
                while ((lines = reader.readLine()) != null) {
                    lines = new String(lines.getBytes(), encoding);
                    sb.append(lines);
                }
                System.out.println("###### HTTPRequest Response:" + sb.toString());
                reader.close();
                closure.execute(sb.toString());
            }else{
                System.out.println("$$$$$$ HTTPRequest Failed, response Code:" + connection.getResponseCode());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendHttpRequest(String api,HTTPParams params, Closure closure) {
        sendHttpRequest(api,params,DefaultEncoding,closure);
    }

    
    
}
