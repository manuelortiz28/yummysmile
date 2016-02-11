package com.visiontech.yummysmile.di.components;

import com.visiontech.yummysmile.di.modules.PresenterModule;
import com.visiontech.yummysmile.ui.presenter.MainPresenter;

import dagger.Subcomponent;

/**
 * @author manuel.ortiz
 *
 * This is a dagger subcomponent which contains references to all dependencies classes related to
 * the presenter tier in the Model View Presenter architecture.
 * This component contains some related modules, such as the Presenter module.
 * Its component parent is the CoreComponent class
 *
 */
@PerActivity
@Subcomponent(modules = {PresenterModule.class})
public interface PresenterComponent {
    MainPresenter getMainPresenter();
}
