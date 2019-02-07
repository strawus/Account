package com.zolotarev.account.controller;

import com.zolotarev.exception.EntityNotFoundException;
import com.zolotarev.exception.InternalException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;

import java.util.Locale;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Transforming any exception from application to response for client
 */
@RestControllerAdvice
@RequiredArgsConstructor
public class AdviceController {

    private final @NonNull MessageSource messageSource;

    /**
     * Transforms {@link IllegalArgumentException} or {@link ValidationException} to http status 400 - client error
     * @param e Exception with information about error
     * @return Entity with http status 400 and localized additional information
     */
    @ExceptionHandler({IllegalArgumentException.class, ValidationException.class})
    public ResponseEntity<?> clientError(final RuntimeException e, final Locale locale) {
        final String localizedMessage = messageSource.getMessage(e.getMessage(), null, locale);
        return new ResponseEntity<>(localizedMessage, BAD_REQUEST);
    }

    /**
     * Transforms {@link EntityNotFoundException} to http 404 - not found error
     * @param e Exception with information about error
     * @return Entity with http status 404 and localized additional information
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> notFoundError(final EntityNotFoundException e, final Locale locale) {
        final String localizedMessage = messageSource.getMessage(e.getMessage(), null, locale);
        return new ResponseEntity<>(localizedMessage, NOT_FOUND);
    }

    /**
     * Transforms {@link InternalException} to http status 500 - internal server error
     */
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalException.class)
    public void serverError() { }
}
