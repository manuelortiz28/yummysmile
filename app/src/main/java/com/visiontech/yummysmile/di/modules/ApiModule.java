package com.visiontech.yummysmile.di.modules;

import com.visiontech.yummysmile.ui.controller.MealsController;
import com.visiontech.yummysmile.ui.controller.MealsControllerImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author manuel.ortiz
 *
 * Module providing API objects
 *
 */
@Module
public class ApiModule {
    @Provides
    @Singleton
    public MealsController provideMealsController() {
        return new MealsControllerImpl();
    }
}
