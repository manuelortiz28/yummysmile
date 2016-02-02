package com.visiontech.yummysmile.di.components;

import android.content.Context;

import com.visiontech.yummysmile.YummySmileApplication;
import com.visiontech.yummysmile.di.modules.ApiModule;
import com.visiontech.yummysmile.di.modules.AppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author manuel.ortiz
 *
 * Component for Core objects
 *
 */
@Singleton
@Component(modules = {AppModule.class, ApiModule.class})
public interface CoreComponent {
    void inject(YummySmileApplication application);

    ApiClientComponent getApiClientComponent();
    Context getContext();
    //Here you can add some subcomponents using the plus method
    //e.i. SomeSubComponent plus(SomeSubComponentModules module1, ...)
}
