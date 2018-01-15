package com.chinatelecom.pimtest.interfaces;

/**
 * Created by Shuo on 2016/10/8.
 */

public interface Predicate<T> {

    public static final Predicate TRUE = new Predicate() {
        @Override
        public boolean eval(Object input) {
            return true;
        }
    };

    boolean eval(T input);
}
