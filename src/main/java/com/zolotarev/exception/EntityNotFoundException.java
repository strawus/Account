package com.zolotarev.exception;

/**
 * Used if entity not found in a storage
 * It may be expected event, in this case you need to handle this exception in try-catch block
 */
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String messageCode) {
        super(messageCode);
    }
}
