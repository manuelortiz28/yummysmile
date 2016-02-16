package com.visiontech.yummysmile.di.modules;

import com.visiontech.yummysmile.repository.api.FactoryRestAdapter;
import com.visiontech.yummysmile.repository.api.MealAPIService;
import com.visiontech.yummysmile.repository.api.UserAPIService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author manuel.ortiz
 *
 * Dagger Module providing API objects dependencies
 *
 */
@Module
public class ApiModule {
    @Provides
    @Singleton
    public MealAPIService provideMealsAPI() {
        return FactoryRestAdapter.createRetrofitService(MealAPIService.class);
    }

    @Provides
    @Singleton
    public UserAPIService providesUserAPI() {
        return FactoryRestAdapter.createRetrofitService(UserAPIService.class);
    }
}
