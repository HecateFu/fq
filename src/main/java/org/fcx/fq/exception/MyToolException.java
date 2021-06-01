package org.fcx.fq.exception;

public class MyToolException extends RuntimeException {
    public MyToolException(String message) {
        super(message);
    }
    public MyToolException(String message, Throwable e) {
        super(message,e);
    }
}
