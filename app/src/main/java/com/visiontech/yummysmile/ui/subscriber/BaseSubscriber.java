package com.visiontech.yummysmile.ui.subscriber;

import android.util.Log;

import com.visiontech.yummysmile.ui.presenter.BasePresenter;

import rx.Subscriber;

/**
 * Class that is going to hanlde the response of Meals Request.
 *
 * @author hector.torres
 */
public class BaseSubscriber<T> extends Subscriber<T> {
    private static final String LOG_TAG = BaseSubscriber.class.getName();
    private BasePresenter presenter;

    public BaseSubscriber(BasePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onCompleted() {
        Log.d(LOG_TAG, "onCompleted()");
    }

    @Override
    public void onError(Throwable e) {
        Log.d(LOG_TAG, "onError()");
        presenter.onError(e);
    }

    @Override
    public void onNext(T data) {
        Log.d(LOG_TAG, "onNext()");
        presenter.onNext(data);
    }
}
