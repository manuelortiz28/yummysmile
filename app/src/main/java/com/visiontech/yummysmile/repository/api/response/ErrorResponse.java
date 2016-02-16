package com.visiontech.yummysmile.repository.api.response;

import java.util.ArrayList;
import java.util.List;

/**
 * @author manuel.ortiz
 *
 * Model representing the error object returned from any API Method
 *
 */
public class ErrorResponse {
    private final HttpResponseCode code;
    private final String message;
    private List<ErrorResponseItem> errors;

    public ErrorResponse(HttpResponseCode code, String message, List<ErrorResponseItem> errors) {
        this.code = code;
        this.message = message;
        this.errors = errors;
    }

    /**
     * Returns the error code represented as an HttpResponseCode enum
     * @return
     */
    public HttpResponseCode getCode() {
        return code;
    }

    /**
     * Returns the message error
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns a list of details errors from the api method
     * @return List of ErrorResponse items, the list is never going to be null
     */
    public List<ErrorResponseItem> getErrors() {
        if (errors == null) {
            errors = new ArrayList<>();
        }
        return errors;
    }
}
