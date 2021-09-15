package com.github.mimsic.base.common.utility;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class ExceptionUtil {

    public static <X extends Throwable> Throwable[] suppressedExceptions(X ex) {

        return ex.getSuppressed();
    }

    public static <X extends Throwable, R extends Throwable> R wrapWithRootCauseMessage(X ex, Class<R> clazz) {

        String message = RegExUtils.removeAll(ExceptionUtils.getRootCause(ex).getMessage(), "[\\r\\n\"]");

        try {
            return clazz.getConstructor(String.class).newInstance(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <X extends Throwable, R extends Throwable> R wrap(X ex, Class<R> clazz) {

        try {
            return clazz.getConstructor(String.class).newInstance(ex.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
