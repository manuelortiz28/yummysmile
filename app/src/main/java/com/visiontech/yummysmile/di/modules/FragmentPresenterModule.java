package com.visiontech.yummysmile.di.modules;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.visiontech.yummysmile.di.scopes.PerFragment;
import com.visiontech.yummysmile.ui.presenter.view.fragment.BaseFragmentView;
import com.visiontech.yummysmile.ui.presenter.view.fragment.CreateAccountView;
import com.visiontech.yummysmile.ui.presenter.view.fragment.CreateMealFragmentView;
import com.visiontech.yummysmile.ui.presenter.view.fragment.HomeFragmentView;
import com.visiontech.yummysmile.ui.presenter.view.fragment.LoginView;
import com.visiontech.yummysmile.ui.presenter.view.fragment.RecoverPasswordFragmentView;

import dagger.Module;
import dagger.Provides;

/**
 * Module containing Views dependencies in the MVP pattern, but only for PerFragment Scope.
 *
 * @author manuel.ortiz
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

    @Provides
    @PerFragment
    @Nullable
    CreateMealFragmentView provideCreateMealFragmentView() {
        if (baseFragment instanceof CreateMealFragmentView) {
            return (CreateMealFragmentView) baseFragment;
        }
        return null;
    }

    @Provides
    @PerFragment
    @Nullable
    RecoverPasswordFragmentView provideRecoverPasswordFragmentView() {
        if (baseFragment instanceof RecoverPasswordFragmentView) {
            return (RecoverPasswordFragmentView) baseFragment;
        }
        return null;
    }

    @Provides
    @PerFragment
    CreateAccountView provideCreateAccountFragmentView() {
        if (baseFragment instanceof CreateAccountView) {
            return (CreateAccountView) baseFragment;
        }
        return null;
    }

    @Provides
    @PerFragment
    LoginView provideLoginFragmentView() {
        if (baseFragment instanceof LoginView) {
            return (LoginView) baseFragment;
        }
        return null;
    }
}
