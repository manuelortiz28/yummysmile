package com.visiontech.yummysmile.repository.api.subscriber;

import android.util.Log;

import com.google.common.base.Preconditions;
import com.visiontech.yummysmile.repository.api.response.BaseResponse;
import com.visiontech.yummysmile.util.ExceptionsUtil;

import rx.Subscriber;

/**
 * Base class that is going to handle the events of Rx Android
 *
 * @author hector.torres
 */
@SuppressWarnings("unchecked")
public abstract class BaseSubscriber<T> extends Subscriber<T> {
    private static final String LOG_TAG = BaseSubscriber.class.getName();

    private final ResultListener resultListener;
    private final BaseResponse baseResponse;

    /**
     * Method call after the service responded successfully and before we notify to the subscriber
     * @param serviceResponse Object containing the response
     */
    protected abstract void onSuccess(T serviceResponse);

    public BaseSubscriber(ResultListener resultListener, BaseResponse responseObject) {
        Preconditions.checkNotNull(resultListener, "ResultListener could not be null");
        Preconditions.checkNotNull(responseObject, "ResponseObject could not be null");

        this.resultListener = resultListener;
        this.baseResponse = responseObject;
    }

    @Override
    public void onCompleted() {
        Log.d(LOG_TAG, "onCompleted()");
    }

    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    @Override
    public void onNext(T data) {
        Log.d(LOG_TAG, "onNext()");

        try {
            //Sets null, in order of change success flag to true
            baseResponse.setPayload(null);
            //Notify to the subscriber that it received the info correctly
            onSuccess(data);

            resultListener.onResult(baseResponse);
        } catch (Exception exception) {
            onError(exception);
        }
    }

    @Override
    public void onError(Throwable e) {
        Log.d(LOG_TAG, "onError()");

        baseResponse.setError(ExceptionsUtil.getSpecificException(e));
        resultListener.onResult(baseResponse);
    }
}
