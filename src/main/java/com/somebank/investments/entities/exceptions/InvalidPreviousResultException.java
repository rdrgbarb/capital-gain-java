package com.somebank.investments.entities.exceptions;

public class InvalidPreviousResultException extends IllegalArgumentException {

    public static final String ERROR_MESSAGE_INVALID_PREVIOUS_RESULT = "Previous operation cannot null or actual quantity cannot be greater than quantity from previous operation.";

    public InvalidPreviousResultException() {
        super(ERROR_MESSAGE_INVALID_PREVIOUS_RESULT);
    }
}
