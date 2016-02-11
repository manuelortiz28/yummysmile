package com.visiontech.yummysmile.di.components;

import android.content.Context;

import com.visiontech.yummysmile.YummySmileApplication;
import com.visiontech.yummysmile.di.modules.ApiModule;
import com.visiontech.yummysmile.di.modules.AppModule;
import com.visiontech.yummysmile.di.modules.PresenterModule;

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

    PresenterComponent plus(PresenterModule presenterModule);
}
