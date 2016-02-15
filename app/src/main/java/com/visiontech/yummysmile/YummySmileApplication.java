package com.visiontech.yummysmile;

import android.app.Application;

import com.visiontech.yummysmile.di.components.CoreComponent;
import com.visiontech.yummysmile.di.components.PresenterComponent;
import com.visiontech.yummysmile.di.modules.AppModule;
import com.visiontech.yummysmile.di.modules.PresenterModule;
import com.visiontech.yummysmile.ui.activity.BaseActivity;

/**
 * @author hector.torres
 */
public class YummySmileApplication extends Application {
    private CoreComponent coreComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        setupGraph();
    }

    /**
     * Initializes dagger components
     */
    private void setupGraph() {
       /* coreComponent =
                DaggerCoreComponent.builder()
                        .appModule(new AppModule(this))
                        .build();*/
        coreComponent.inject(this);
    }

    /**
     * Gets Core component
     * @return The Dagger Core Component
     */
    public CoreComponent getCoreComponent() {
        return coreComponent;
    }

    /**
     * Gets the Presenter component
     * @param activity implementing any View interface from the MVP pattern
     * @return The Dagger presenter component
     */
    public PresenterComponent getPresenterComponent(BaseActivity activity) {
        return coreComponent.plus(new PresenterModule(activity));
    }
}
