package com.chinatelecom.pimtest.net;

/**
 * Factory for http template
 *
 * @author snowway
 * @since 4/21/11
 */
public interface HttpTemplateFactory {

    /**
     * @return HttpTemplate
     */
    HttpTemplate getHttpTemplate();
    HttpTemplate getHttpTemplate(int connectionTimeOut, int soTimeOut);
}
