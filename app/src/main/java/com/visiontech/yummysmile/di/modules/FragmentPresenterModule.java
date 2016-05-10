package com.visiontech.yummysmile.di.modules;

import android.app.Fragment;
import android.support.annotation.Nullable;

import com.visiontech.yummysmile.di.scopes.PerFragment;
import com.visiontech.yummysmile.ui.presenter.view.fragment.BaseFragmentView;
import com.visiontech.yummysmile.ui.presenter.view.fragment.HomeFragmentView;

import dagger.Module;
import dagger.Provides;

/**
 * @author manuel.ortiz
 *
 * Module containing Views dependencies in the MVP pattern, but only for PerFragment Scope
 *
 */
@Module
public class FragmentPresenterModule {
    private final Fragment baseFragment;

    public FragmentPresenterModule(Fragment fragment) {
        this.baseFragment = fragment;
    }

    @Provides
    @PerFragment
    @Nullable
    HomeFragmentView provideHomeFragmentView() {
        if (baseFragment instanceof HomeFragmentView) {
            return (HomeFragmentView) baseFragment;
        }
        return null;
    }

    @Provides
    @PerFragment
    @Nullable
    BaseFragmentView provideBaseFragmentView() {
        if (baseFragment instanceof BaseFragmentView) {
            return (BaseFragmentView) baseFragment;
        }
        return null;
    }
}
