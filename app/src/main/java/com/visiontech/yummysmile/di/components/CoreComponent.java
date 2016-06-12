package com.visiontech.yummysmile.di.components;

import android.content.Context;

import com.visiontech.yummysmile.YummySmileApplication;
import com.visiontech.yummysmile.di.modules.ApiModule;
import com.visiontech.yummysmile.di.modules.AppModule;
import com.visiontech.yummysmile.repository.api.MealAPIService;
import com.visiontech.yummysmile.repository.api.UserAPIService;
import com.visiontech.yummysmile.ui.controller.AuthenticationControllerImpl;
import com.visiontech.yummysmile.util.PermissionsHelper;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author manuel.ortiz
 *
 * This is a dagger component containing references to dependencies classes related to
 * the core functionalities in the whole app. This component contains some related modules,
 * which exposes some dependencies.
 *
 * Components basically are injectors, let’s say a bridge between @Inject and @Module,
 * which its main responsibility is to put both together.
 *
 */
@Singleton
@Component(modules = {AppModule.class, ApiModule.class})
public interface CoreComponent {
    void inject(YummySmileApplication application);

    Context getContext();
    YummySmileApplication getApplication();
    MealAPIService getMealAPIService();
    UserAPIService getUserAPIService();
    AuthenticationControllerImpl getAuthenticationController();
    PermissionsHelper getPermissionsHelper();
}
