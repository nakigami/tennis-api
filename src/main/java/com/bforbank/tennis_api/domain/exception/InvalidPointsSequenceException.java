package com.bforbank.tennis_api.domain.exception;

public class InvalidPointsSequenceException extends RuntimeException {
    public InvalidPointsSequenceException(String message) {
        super(message);
    }
}
