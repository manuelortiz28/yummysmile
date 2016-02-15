package com.visiontech.yummysmile.repository.api.response;

/**
 * Base class to handle the response of every request.
 *
 * @author hector.torres
 */
public class BaseResponse<T> {

    private boolean success;
    private T payload;
    private ErrorResponse error;

    public void setPayload(T payload) {
        this.payload = payload;
        this.success = true;
    }

    public void setError(ErrorResponse error) {
        this.error = error;
        this.success = false;
    }

    public boolean isSuccess() {
        return success;
    }

    public T getPayload() {
        return payload;
    }

    public ErrorResponse getError() {
        return error;
    }
}
