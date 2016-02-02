package com.visiontech.yummysmile;

import android.app.Application;

import com.visiontech.yummysmile.di.components.ApiClientComponent;
import com.visiontech.yummysmile.di.components.CoreComponent;
import com.visiontech.yummysmile.di.components.DaggerCoreComponent;
import com.visiontech.yummysmile.di.modules.AppModule;

/**
 * @author hector.torres
 */
public class YummySmileApplication extends Application {
    private CoreComponent coreComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        init();
    }

    private void init() {
        setupGraph();
    }

    private void setupGraph() {
        coreComponent = DaggerCoreComponent.builder().appModule(new AppModule(this)).build();
        coreComponent.inject(this);
    }

    public CoreComponent getCoreComponent() {
        return coreComponent;
    }

    public ApiClientComponent getApiClientComponent() {
        return coreComponent.getApiClientComponent();
    }
}
