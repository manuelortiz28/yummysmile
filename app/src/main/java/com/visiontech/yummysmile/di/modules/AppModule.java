package com.visiontech.yummysmile.di.modules;

import android.app.Application;
import android.content.Context;

import com.facebook.CallbackManager;
import com.visiontech.yummysmile.YummySmileApplication;
import com.visiontech.yummysmile.ui.controller.AuthenticationController;
import com.visiontech.yummysmile.ui.controller.AuthenticationControllerImpl;
import com.visiontech.yummysmile.ui.controller.MealsController;
import com.visiontech.yummysmile.ui.controller.MealsControllerImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author manuel.ortiz
 *         <p/>
 *         Module providing objects for the App
 */
@Module
public class AppModule {
    private final Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    YummySmileApplication provideYummySmileApplication() {
        return (YummySmileApplication) application;
    }

    @Provides
    @Singleton
    Context provideAppContext() {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    AuthenticationController provideAuthenticationController(AuthenticationControllerImpl authenticationController) {
        return authenticationController;
    }

    @Provides
    @Singleton
    MealsController provideMealController(MealsControllerImpl mealsController) {
        return mealsController;
    }

    @Provides
    @Singleton
    CallbackManager provideCallBackManager() {
        return CallbackManager.Factory.create();
    }
}
