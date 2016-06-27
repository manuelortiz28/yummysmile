package com.visiontech.yummysmile.di.components;

import com.visiontech.yummysmile.di.modules.FragmentPresenterModule;
import com.visiontech.yummysmile.di.scopes.PerFragment;
import com.visiontech.yummysmile.ui.fragments.HomeFragment;
import com.visiontech.yummysmile.ui.presenter.BaseFragmentPresenter;
import com.visiontech.yummysmile.ui.presenter.CreateAccountPresenter;
import com.visiontech.yummysmile.ui.presenter.CreateMealPresenter;
import com.visiontech.yummysmile.ui.presenter.HomePresenter;
import com.visiontech.yummysmile.ui.presenter.LoginPresenter;
import com.visiontech.yummysmile.ui.presenter.RecoverPasswordPresenter;

import dagger.Component;

/**
 * @author manuel.ortiz
 *
 * This is a dagger subcomponent which contains references to all dependencies classes related to
 * the presenter tier in the Model View Presenter architecture.
 * This component contains some related modules, such as the Presenter module.
 * Its component parent is the CoreComponent class
 *
 */
@PerFragment
@Component(dependencies = ActivityPresenterComponent.class, modules = {FragmentPresenterModule.class})
public interface FragmentPresenterComponent {
    void inject(HomeFragment homeFragment);

    HomePresenter getMainPresenter();
    LoginPresenter getLoginPresenter();
    RecoverPasswordPresenter getRecoverPasswordPresenter();
    CreateMealPresenter getCreateMealPresenter();
    CreateAccountPresenter getCreateAccountPresenter();
    BaseFragmentPresenter getBaseFragmentPresenter();
}
