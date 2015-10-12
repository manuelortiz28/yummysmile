package com.visiontech.yummysmile.ui.controller.events;

import com.google.common.base.Preconditions;

import retrofit.Response;

/**
 * @author hector.torres
 */
public class BaseResponseEvent<T> {
    boolean success;
    T response; //using custom response object acording to the request.
    Throwable throwable;

    public void setResult(T response){
        Preconditions.checkNotNull(response, "The response cannot be null on a success event.");
        this.success = true;
        this.response = response;
    }

    public void setResult(Throwable throwable){
        Preconditions.checkNotNull(throwable, "The exception cannot be null on a fail event.");
        this.success = false;
        this.throwable = throwable;
    }

    public T getResponse(){
        return response;
    }

    public boolean isSuccess(){
        return success;
    }

    public Throwable getThrowable(){
        return throwable;
    }
}
