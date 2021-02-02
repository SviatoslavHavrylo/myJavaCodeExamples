package com.sviatoslavHavrylo.api.exception;

public class ApiLogicFailException extends RuntimeException {

    private static final long serialVersionUID = 750326696906279492L;

    public ApiLogicFailException() {
        super();
    }

    public ApiLogicFailException(String message, Throwable cause, boolean enableSuppression,
                                 boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ApiLogicFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiLogicFailException(String message) {
        super(message);
    }

    public ApiLogicFailException(Throwable cause) {
        super(cause);
    }
}
