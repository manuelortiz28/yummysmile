package com.visiontech.yummysmile.di.modules;

import android.app.Activity;
import android.support.annotation.Nullable;

import com.visiontech.yummysmile.di.scopes.PerActivity;
import com.visiontech.yummysmile.ui.presenter.view.activity.DrawerActivityView;
import com.visiontech.yummysmile.ui.presenter.view.activity.LoginActivityView;

import dagger.Module;
import dagger.Provides;

/**
 * @author manuel.ortiz
 *
 * Module containing Views dependencies in the MVP pattern, but only for PerActivity Scope
 *
 */
@Module
public class ActivityPresenterModule {
    private final Activity baseActivity;

    public ActivityPresenterModule(Activity activity) {
        this.baseActivity = activity;
    }

    @Provides
    @PerActivity
    @Nullable
    DrawerActivityView providesDrawerView() {
        if (baseActivity instanceof DrawerActivityView) {
            return (DrawerActivityView) baseActivity;
        }

        return null;
    }

    @Provides
    @PerActivity
    @Nullable
    LoginActivityView providesLoginView() {
        if (baseActivity instanceof LoginActivityView) {
            return (LoginActivityView) baseActivity;
        }

        return null;
    }
}
