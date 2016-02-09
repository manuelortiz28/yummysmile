package com.visiontech.yummysmile.di.modules;

import com.visiontech.yummysmile.repository.api.FactoryRestAdapter;
import com.visiontech.yummysmile.repository.api.MealAPIService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author manuel.ortiz
 *         <p/>
 *         Module providing API objects
 */
@Module
public class ApiModule {
    @Provides
    @Singleton
    public MealAPIService provideMealsAPI() {
        return FactoryRestAdapter.createRetrofitService(MealAPIService.class);
    }
}
