package com.zolotarev.exception;

/**
 * Used if any error occurs in an application
 */
public class InternalException extends RuntimeException {

    public InternalException(String message, Throwable cause) {
        super(message, cause);
    }
}
