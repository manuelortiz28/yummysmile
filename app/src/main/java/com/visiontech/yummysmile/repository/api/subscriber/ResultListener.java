package com.visiontech.yummysmile.repository.api.subscriber;

/**
 * Listener to handle the result of a call on the Presenter.
 *
 * @author hector.torres
 */
public interface ResultListener<T> {
    void onResult(T result);
}
