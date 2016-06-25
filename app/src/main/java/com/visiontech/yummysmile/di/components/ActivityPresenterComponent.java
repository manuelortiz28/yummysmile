package com.visiontech.yummysmile.di.components;

import android.support.annotation.Nullable;

import com.visiontech.yummysmile.di.modules.ActivityPresenterModule;
import com.visiontech.yummysmile.di.scopes.PerActivity;
import com.visiontech.yummysmile.ui.activity.DrawerActivity;
import com.visiontech.yummysmile.ui.presenter.view.activity.AuthenticatorActivityView;
import com.visiontech.yummysmile.ui.presenter.view.activity.BaseActivityView;

import dagger.Component;

/**
 * Created by manuel on 02/03/16.
 */
@PerActivity
@Component(dependencies = CoreComponent.class, modules = {ActivityPresenterModule.class})
public interface ActivityPresenterComponent extends CoreComponent {
    void inject(DrawerActivity drawerActivity);

    @Nullable
    BaseActivityView getBaseActivityView();

    @Nullable
    AuthenticatorActivityView getAuthenticatorActivityView();
}
