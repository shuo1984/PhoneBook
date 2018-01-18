package com.chinatelecom.pimtest.net;

import org.apache.http.impl.client.DefaultHttpClient;

/**
 * factory class for HttpClient
 *
 * @author snowway
 * @since 4/14/11
 */
public interface HttpClientFactory {

    /**
     * @return apache http core DefaultHttpClient
     */
    DefaultHttpClient create();
}
