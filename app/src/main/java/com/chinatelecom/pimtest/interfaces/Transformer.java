package com.chinatelecom.pimtest.interfaces;

/**
 * Created by Shuo on 2016/10/8.
 */

public interface Transformer<F, T> {

    T transform(F input);
}
