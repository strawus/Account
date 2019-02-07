package com.zolotarev.exception;

/**
 * Used if any error occurs in an external service
 */
public class ExternalException extends RuntimeException {
    public ExternalException(String message) {
        super(message);
    }
}
