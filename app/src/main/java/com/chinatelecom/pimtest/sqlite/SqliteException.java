package com.chinatelecom.pimtest.sqlite;


import com.chinatelecom.pimtest.exceptions.FoundationException;

/**
 * exception for sqlite
 *
 * @author snowway
 * @since 2/24/11
 */
public class SqliteException extends FoundationException {

    private static final long serialVersionUID = -6881204672547625482L;


    public SqliteException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
