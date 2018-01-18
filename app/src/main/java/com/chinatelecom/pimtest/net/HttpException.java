package com.chinatelecom.pimtest.net;


/**
 * root exception of http
 *
 * @author snowway
 * @since 4/8/11
 */
public class HttpException extends FoundationException {

    private static final long serialVersionUID = 130748170869573919L;

    public HttpException() {
    }

    public HttpException(String s) {
        super(s);
    }

    public HttpException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public HttpException(Throwable throwable) {
        super(throwable);
    }
}
