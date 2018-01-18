package com.chinatelecom.pimtest.net;

import org.apache.http.impl.client.DefaultHttpClient;

/**
 * @author snowway
 * @since 4/8/11
 */
public class DefaultHttpClientFactory implements HttpClientFactory {

    @Override
    public DefaultHttpClient create() {
        return SSLSocketFactoryEx.getNewHttpClient();
    }
}
