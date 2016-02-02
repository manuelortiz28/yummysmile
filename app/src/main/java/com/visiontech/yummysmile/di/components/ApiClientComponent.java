package com.visiontech.yummysmile.di.components;

import com.visiontech.yummysmile.ui.controller.MealsController;

import javax.inject.Singleton;

import dagger.Subcomponent;

/**
 * @author manuel.ortiz
 *
 * Component for ApiClient objects
 *
 */
@Singleton
@Subcomponent
public interface ApiClientComponent {
    MealsController getMealsController();
}
