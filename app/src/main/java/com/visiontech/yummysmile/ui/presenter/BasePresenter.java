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
    @Inject
    YummySmileApplication application;

    protected Context getContext() {
        return application.getCoreComponent().getContext();
    }
}
