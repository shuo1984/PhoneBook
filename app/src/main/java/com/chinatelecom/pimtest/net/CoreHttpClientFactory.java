package com.chinatelecom.pimtest.net;



import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

/**
 * @author owensun
 * @since 09/12/2014
 */
public class CoreHttpClientFactory extends DefaultHttpClientFactory {

    @Override
    public DefaultHttpClient create() {
        DefaultHttpClient defaultHttpClient = super.create();
        defaultHttpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
        defaultHttpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
//        defaultHttpClient.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY, new HttpHost("10.0.0.200", 80));
        return defaultHttpClient;
    }
}
