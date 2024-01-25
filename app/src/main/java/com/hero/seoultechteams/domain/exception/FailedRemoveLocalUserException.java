package com.hero.seoultechteams.domain.exception;

public class FailedRemoveLocalUserException extends Exception {
    public FailedRemoveLocalUserException(String message) {
        super(message);
    }

    public FailedRemoveLocalUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
