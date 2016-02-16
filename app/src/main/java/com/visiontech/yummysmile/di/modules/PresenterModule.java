package com.visiontech.yummysmile.di.modules;

import com.visiontech.yummysmile.di.components.PerActivity;
import com.visiontech.yummysmile.ui.activity.BaseActivity;
import com.visiontech.yummysmile.ui.presenter.LoginView;
import com.visiontech.yummysmile.ui.presenter.MainView;

import dagger.Module;
import dagger.Provides;

/**
 * @author manuel.ortiz
 *
 * Module containing views dependencies
 *
 */
@Module
public class PresenterModule {
    private final BaseActivity activity;

    public PresenterModule(BaseActivity activity) {
        this.activity = activity;
    }

    @Provides
    @PerActivity
    MainView providesMainView() {
        return (MainView) activity;
    }

    @Provides
    @PerActivity
    LoginView providesLoginView() {
        return (LoginView) activity;
    }
}
