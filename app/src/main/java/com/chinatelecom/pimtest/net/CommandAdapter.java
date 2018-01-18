package com.chinatelecom.pimtest.net;

/**
 * @author owensun
 * @since 07/03/2012
 */
public interface CommandAdapter<T, V> {

    T prepare();

    void onComplete(V v);

    void onComplete(byte[] bytes);

}
