package com.chinatelecom.pimtest.net;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author snowway
 * @since 4/8/11
 */
public abstract class DefaultHttpCallback implements HttpCallback {

    private static final Logger logger = LoggerFactory.getLogger(DefaultHttpCallback.class.getSimpleName());

    public static final String CHARSET = "utf-8";

    protected HttpUrlParamsBuiler newUrlParamsBuiler() {
        return new HttpUrlParamsBuiler();
    }

    protected HttpFormBuilder newFormBuiler() {
        return new HttpFormBuilder();
    }
/*


   protected HttpMultipartBuilder newMultipartBuilder() {
        return new HttpMultipartBuilder();
    }
*/


    protected String getResponseAsString(HttpResponse response) {
        try {
            return EntityUtils.toString(response.getEntity());
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
            throw new HttpException(ex.getMessage(), ex);
        }
    }

    /**
     * url参数构造器
     */
    protected static class HttpUrlParamsBuiler {

        private HttpParams params;

        public HttpUrlParamsBuiler() {
            params = new BasicHttpParams();
        }

        public HttpUrlParamsBuiler add(String param, String value) {
            params.setParameter(param, value);
            return this;
        }

        public HttpParams build() {
            return this.params;
        }
    }

    /**
     * form参数构造器
     */
    protected static class HttpFormBuilder {

        private List<NameValuePair> pairs;

        public HttpFormBuilder() {
            pairs = new ArrayList<NameValuePair>();
        }

        public HttpFormBuilder add(String name, String value) {
            pairs.add(new BasicNameValuePair(name, value));
            return this;
        }

        public UrlEncodedFormEntity build() {
            return build(CHARSET);
        }

        public UrlEncodedFormEntity build(String encoding) {
            try {
                return new UrlEncodedFormEntity(pairs, encoding);
            } catch (UnsupportedEncodingException ex) {
                throw new HttpException(ex.getMessage(), ex);
            }
        }
    }

   /* protected static class HttpMultipartBuilder {

        private MultipartEntity entity;

        public HttpMultipartBuilder() {
            entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        }

        public HttpMultipartBuilder add(String name, String value) {
            try {
                entity.addPart(name, new StringBody(value, Charset.forName(CHARSET)));
            } catch (UnsupportedEncodingException ex) {
                throw new HttpException(ex.getMessage(), ex);
            }
            return this;
        }

        public HttpMultipartBuilder add(String name, InputStream input, String mimetype) {
            entity.addPart(name, new InputStreamBody(input, mimetype));
            return this;
        }

        public HttpMultipartBuilder add(String name, File file, String mimetype) {
            entity.addPart(name, new FileBody(file, mimetype));
            return this;
        }

        public HttpMultipartBuilder add(String name, File file) {
            entity.addPart(name, new FileBody(file));
            return this;
        }

        public MultipartEntity build() {
            return entity;
        }

    }*/

    /**
     * 创建cookie store context
     * usage:
     * httpclient.execute(post,createCookieStoreContext(httpclient.getCookieStore));
     *
     * @param cookieStore CookieStore
     * @return HttpContext
     */
    protected HttpContext createCookieStoreContext(CookieStore cookieStore) {
        HttpContext context = new BasicHttpContext();
        context.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
        return context;
    }


    /**
     * SC_OK(200)
     *
     * @param response HttpResponse
     * @return is status ok
     */
    protected boolean isStatusOK(HttpResponse response) {
        return response.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
    }

}
