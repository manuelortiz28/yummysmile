package com.visiontech.yummysmile.di.components;

import com.visiontech.yummysmile.di.modules.PresenterModule;
import com.visiontech.yummysmile.ui.presenter.MainPresenter;

import dagger.Subcomponent;

/**
 * @author manuel.ortiz
 */
@PerActivity
@Subcomponent(modules = {PresenterModule.class})
public interface PresenterComponent {
    MainPresenter getMainPresenter();
}
