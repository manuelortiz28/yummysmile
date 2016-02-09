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
 *         <p/>
 *         Component for Core objects
 */
@Singleton
@Component(modules = {AppModule.class, ApiModule.class})
public interface CoreComponent {
    void inject(YummySmileApplication application);

    Context getContext();

    PresenterComponent plus(PresenterModule presenterModule);
}
