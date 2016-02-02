package com.visiontech.yummysmile.di.modules;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author manuel.ortiz
 *
 * Module providing objects for the App
 *
 */
@Module
public class AppModule {
    private final Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideAppContext() {
        return application.getApplicationContext();
    }
}
