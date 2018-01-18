package com.chinatelecom.pimtest.net;

import org.apache.http.client.HttpClient;

/**
 * call back interface for android apache http
 *
 * @author snowway
 * @since 4/8/11
 */
public interface HttpCallback {

    void call(HttpClient client) throws Exception;
}
