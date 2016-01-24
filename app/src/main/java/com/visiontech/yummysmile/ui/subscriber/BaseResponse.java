package com.visiontech.yummysmile.ui.subscriber;

/**
 * Base class to handle the response of every request.
 *
 * @author hector.torres
 */
public class BaseResponse<T> {

    private boolean success;
    private T payload;
    private Throwable error;

    public void setPayload(T payload) {
        this.payload = payload;
        this.success = true;
    }

    public void setError(Throwable error) {
        this.error = error;
        this.success = false;
    }

    public boolean isSuccess() {
        return success;
    }

    public T getPayload() {
        return payload;
    }

    public Throwable getError() {
        return error;
    }


}
