package com.rajorshi.samay.dispatchers;

public class DispatcherException extends Exception {

    public DispatcherException(String message) {
        super(message);
    }

    public DispatcherException(String message, Throwable cause) {
        super(message, cause);
    }

    public DispatcherException(Throwable cause) {
        super(cause);
    }

    public DispatcherException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
