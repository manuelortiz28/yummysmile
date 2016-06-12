package com.visiontech.yummysmile;

import android.app.Application;

import com.visiontech.yummysmile.di.components.ActivityPresenterComponent;
import com.visiontech.yummysmile.di.components.CoreComponent;
import com.visiontech.yummysmile.di.components.DaggerActivityPresenterComponent;
import com.visiontech.yummysmile.di.components.DaggerCoreComponent;
import com.visiontech.yummysmile.di.components.DaggerFragmentPresenterComponent;
import com.visiontech.yummysmile.di.components.FragmentPresenterComponent;
import com.visiontech.yummysmile.di.scopes.PerApplication;
import com.visiontech.yummysmile.di.modules.ActivityPresenterModule;
import com.visiontech.yummysmile.di.modules.AppModule;
import com.visiontech.yummysmile.di.modules.FragmentPresenterModule;
import com.visiontech.yummysmile.ui.activity.BaseActivity;
import com.visiontech.yummysmile.ui.fragments.BaseFragment;

/**
 * @author hector.torres
 */
@PerApplication
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

        coreComponent = DaggerCoreComponent.builder()
                            .appModule(new AppModule(this))
                            .build();

        coreComponent.inject(this);
    }

    /**
     * Gets Core component
     *
     * @return The Dagger Core Component
     */
    public CoreComponent getCoreComponent() {
        return coreComponent;
    }

    /**
     * Gets the Presenter component
     *
     * @param activity implementing any View interface from the MVP pattern
     * @return The Dagger presenter component
     */
    public ActivityPresenterComponent getActivityPresenterComponent(BaseActivity activity) {

        return DaggerActivityPresenterComponent
                .builder()
                .coreComponent(coreComponent)
                .activityPresenterModule(new ActivityPresenterModule(activity))
                .build();
    }

    /**
     * Gets the Presenter component
     *
     * @param fragment implementing any View interface from the MVP pattern
     * @return The Dagger presenter component
     */
    public FragmentPresenterComponent getFragmentPresenterComponent(
            BaseFragment fragment,
            ActivityPresenterComponent activityPresenterComponent) {

        return DaggerFragmentPresenterComponent
                .builder()
                .activityPresenterComponent(activityPresenterComponent)
                .fragmentPresenterModule(new FragmentPresenterModule(fragment))
                .build();
    }

    /**
     * Gets the Presenter component
     *
     * @param fragment implementing any View interface from the MVP pattern
     * @param activity  implementing any View interface from the MVP pattern
     * @return The Dagger presenter component
     */
    public FragmentPresenterComponent getFragmentPresenterComponent(
            BaseFragment fragment,
            BaseActivity activity) {

        return DaggerFragmentPresenterComponent
                .builder()
                .activityPresenterComponent(getActivityPresenterComponent(activity))
                .fragmentPresenterModule(new FragmentPresenterModule(fragment))
                .build();
    }
}
