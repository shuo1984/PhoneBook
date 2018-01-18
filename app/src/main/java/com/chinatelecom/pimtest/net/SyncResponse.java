package com.chinatelecom.pimtest.net;

/**
 * @author owensun
 * @since 05/02/2013
 */
public class SyncResponse<T>  {

    private boolean ok;

    private int statusCode;

    private int code;

    private String message;

    private T body;

    public SyncResponse() {
    }

    public SyncResponse(T body) {
        this.body = body;
    }

    public void set(boolean ok, int code, int statusCode, String message) {
        this.ok = ok;
        this.code = code;
        this.statusCode = statusCode;
        this.message = message;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
