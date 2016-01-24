package com.visiontech.yummysmile.ui.subscriber;

import android.util.Log;

import rx.Subscriber;

/**
 * Base class that is going to handle the events of Rx Android
 *
 * @author hector.torres
 */
public abstract class BaseSubscriber<T> extends Subscriber<T> {
    private static final String LOG_TAG = BaseSubscriber.class.getName();
    private ResultListener result;

    public BaseSubscriber(ResultListener result) {
        this.result = result;
    }

    public abstract BaseResponse getBaseResponse();

    @Override
    public void onCompleted() {
        Log.d(LOG_TAG, "onCompleted()");
    }

    @Override
    public void onError(Throwable e) {
        Log.d(LOG_TAG, "onError()");
        BaseResponse response = getBaseResponse();
        response.setError(e);
        result.onResult(response);
    }

    @Override
    public void onNext(T data) {
        Log.d(LOG_TAG, "onNext()");
        BaseResponse response = getBaseResponse();
        response.setPayload(data);
        result.onResult(response);
    }
}
