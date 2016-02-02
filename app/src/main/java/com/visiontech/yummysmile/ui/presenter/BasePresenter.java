package com.visiontech.yummysmile.ui.presenter;

import android.content.Context;

import com.visiontech.yummysmile.YummySmileApplication;

/**
 * @author manuel.ortiz
 * Base class for the Presenter tier
 *
 */
public class BasePresenter {
    private final YummySmileApplication application;

    public BasePresenter(YummySmileApplication application) {
        this.application = application;
    }

    protected Context getContext() {
        return application.getCoreComponent().getContext();
    }
}
