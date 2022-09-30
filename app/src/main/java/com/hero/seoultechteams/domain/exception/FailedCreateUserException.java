package com.hero.seoultechteams.domain.exception;

public class FailedCreateUserException extends Exception {
    public FailedCreateUserException(String message, Throwable cause) {
            super(message, cause);
        }
}