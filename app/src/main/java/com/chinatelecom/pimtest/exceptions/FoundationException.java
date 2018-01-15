package com.chinatelecom.pimtest.exceptions;

/**
 * Created by Shuo on 2016/10/8.
 */

public class FoundationException extends RuntimeException {

    private static final long serialVersionUID = -2167705868135900188L;

    public FoundationException() {
    }

    public FoundationException(String s) {
        super(s);
    }

    public FoundationException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public FoundationException(Throwable throwable) {
        super(throwable);
    }
}
