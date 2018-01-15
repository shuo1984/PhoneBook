package com.chinatelecom.pimtest.interfaces;

import com.chinatelecom.pimtest.exceptions.FoundationException;

/**
 * Created by Shuo on 2016/10/8.
 */

public interface Closure<T> {
    public static class VetoException extends FoundationException {
        private static final long serialVersionUID = 7887659760614542342L;
    }

    public static class Execution {
        public static void veto() {
            throw new VetoException();
        }
    }

    boolean execute(T input);
}
