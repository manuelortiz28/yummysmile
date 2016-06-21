package com.visiontech.yummysmile.repository.api.response;

/**
 * @author manuel.ortiz
 *
 * Model representing the error object returned from any API Method
 *
 */
public class ErrorResponseItem {

    /**
     * Different error reason types
     */
    public enum ReasonType {
        UNKNOWN,
        FIELDS_REQUIRED,
        NEW_PASSWORD_INVALID,
        INVALID_CREDENTIALS,
        EMAIL_NOT_CONFIRMED
    };

    private final ReasonType reason;
    private final String message;

    public ErrorResponseItem(ReasonType reason, String message) {
        this.reason = reason;
        this.message = message;
    }

    /**
     * Gets the error reason
     * @return The error reason as an ReasonType value
     */
    public ReasonType getReason() {
        return reason;
    }

    /**
     * Gets the error message
     * @return Error message
     */
    public String getMessage() {
        return message;
    }
}
