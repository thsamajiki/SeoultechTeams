package com.hero.seoultechteams.domain.exception;

public class FailedAddLocalUserException extends Exception {

    public FailedAddLocalUserException(String message) {
        super(message);
    }

    public FailedAddLocalUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
