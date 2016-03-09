package com.visiontech.yummysmile.ui.presenter;

import android.content.Context;

import com.visiontech.yummysmile.YummySmileApplication;

import javax.inject.Inject;

/**
 * @author manuel.ortiz
 * Base class for the Presenter tier
 *
 */
public class BasePresenter {
    private YummySmileApplication application;

    public BasePresenter(YummySmileApplication application) {
        this.application = application;
    }

    @Inject
    public BasePresenter() {
    }

    protected Context getContext() {
        return application.getCoreComponent().getContext();
    }
}
