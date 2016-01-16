package com.visiontech.yummysmile.ui.presenter;

/**
 * Base class to use for Presenters.
 */
public abstract class BasePresenter {
    public abstract <T> void onNext(T data);

    public abstract void onError(Throwable e);
}
