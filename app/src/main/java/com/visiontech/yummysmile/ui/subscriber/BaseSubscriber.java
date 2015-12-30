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
    private BasePresenter presenter;

    public BaseSubscriber(BasePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onCompleted() {
        Log.d("CONTROLLER", "onCompleted");
    }

    @Override
    public void onError(Throwable e) {
        Log.d("CONTROLLER", "onError");
        presenter.onError(e);
    }

    @Override
    public void onNext(T data) {
        Log.d("CONTROLLER", "onNext");
        presenter.onNext(data);
    }
}
