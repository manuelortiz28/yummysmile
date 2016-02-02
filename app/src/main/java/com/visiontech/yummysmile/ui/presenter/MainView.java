package com.visiontech.yummysmile.ui.presenter;

import com.visiontech.yummysmile.repository.api.dto.MealsDTO;

/**
 * Interface to declare the methods to manipulate the main view.
 *
 * @author hetorres
 */
public interface MainView extends BaseView {
    void mealsItems(MealsDTO mealsDTO);
}
