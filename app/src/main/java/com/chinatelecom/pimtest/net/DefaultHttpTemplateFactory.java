package com.chinatelecom.pimtest.net;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

/**
 * abstract provider for HttpTemplate
 *
 * @author snowway
 * @since 4/14/11
 */
public class DefaultHttpTemplateFactory implements HttpTemplateFactory {

    protected HttpClientFactory httpClientFactory = new DefaultHttpClientFactory();

    protected HttpExceptionHandler httpExceptionHandler;

    public HttpTemplateFactory setHttpClientFactory(HttpClientFactory httpClientFactory) {
        this.httpClientFactory = httpClientFactory;
        return this;
    }

    public void setHttpExceptionHandler(HttpExceptionHandler httpExceptionHandler) {
        this.httpExceptionHandler = httpExceptionHandler;
    }

    public HttpTemplate getHttpTemplate() {
        DefaultHttpClient client = httpClientFactory.create();
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 120000);
        HttpTemplate template = new HttpTemplate(client);
        if (httpExceptionHandler != null) {
            template.setExceptionHandler(httpExceptionHandler);
        }
        configHttpClient(client);
        return template;
    }

    public HttpTemplate getHttpTemplate(int connectionTimeOut, int soTimeOut) {
        DefaultHttpClient client = httpClientFactory.create();
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, connectionTimeOut);
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, soTimeOut);
        HttpTemplate template = new HttpTemplate(client);
        if (httpExceptionHandler != null) {
            template.setExceptionHandler(httpExceptionHandler);
        }
        configHttpClient(client);
        return template;
    }

    protected void configHttpClient(DefaultHttpClient client) {

    }
}
