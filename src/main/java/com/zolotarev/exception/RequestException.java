package com.zolotarev.exception;

/**
 * Used if request to an external service contains any error
 */
public class RequestException extends RuntimeException {

    public RequestException(String message) {
        super(message);
    }
}
