package com.visiontech.yummysmile.ui.presenter.view.fragment;

import com.visiontech.yummysmile.models.Meal;

import java.util.List;

/**
 * Interface to declare the methods to manipulate the main view.
 *
 * @author hetorres
 */
public interface HomeFragmentView {

    /**
     * Show the meals in the user interface
     * @param mealList
     */
    void showMealsItems(List<Meal> mealList);

    /**
     * Set if the pull to refresh should be display or not
     * @param refreshing If the pull to refresh should be display or not
     */
    void setRefreshing(boolean refreshing);
}
