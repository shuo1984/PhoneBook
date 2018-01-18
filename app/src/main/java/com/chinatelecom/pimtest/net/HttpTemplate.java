package com.chinatelecom.pimtest.net;

import org.apache.http.client.HttpClient;

/**
 * template class for http
 *
 * @author snowway
 * @since 4/8/11
 */
public class HttpTemplate {

    protected HttpClient httpClient;

    private HttpExceptionHandler exceptionHandler;

    public void setExceptionHandler(HttpExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public HttpTemplate(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public void execute(HttpCallback callback) {
        try {

            callback.call(httpClient);
        } catch (Exception ex) {
            HttpException exception;
            if (ex instanceof HttpException) {
                exception = (HttpException) ex;
            } else {
                exception = new HttpException(ex.getMessage(), ex);
            }
            if (exceptionHandler != null) {
                exceptionHandler.handle(exception);
            } else {
                throw exception;
            }
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }
}
