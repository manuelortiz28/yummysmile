package com.visiontech.yummysmile.di.modules;

import android.app.Activity;
import android.support.annotation.Nullable;

import com.visiontech.yummysmile.di.scopes.PerActivity;
import com.visiontech.yummysmile.ui.presenter.view.activity.AuthenticatorActivityView;
import com.visiontech.yummysmile.ui.presenter.view.activity.BaseActivityView;
import com.visiontech.yummysmile.ui.presenter.view.activity.UserSessionView;

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
    BaseActivityView provideBaseActivityView() {
        if (baseActivity instanceof BaseActivityView) {
            return (BaseActivityView) baseActivity;
        }
        return null;
    }

    @Provides
    @PerActivity
    @Nullable
    UserSessionView provideDrawerView() {
        if (baseActivity instanceof UserSessionView) {
            return (UserSessionView) baseActivity;
        }

        return null;
    }

    @Provides
    @PerActivity
    @Nullable
    AuthenticatorActivityView provideAuthenticatorActivityView() {
        if (baseActivity instanceof AuthenticatorActivityView) {
            return (AuthenticatorActivityView) baseActivity;
        }

        return null;
    }
}
