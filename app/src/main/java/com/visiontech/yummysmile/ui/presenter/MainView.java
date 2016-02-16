package com.visiontech.yummysmile.ui.presenter;

import com.visiontech.yummysmile.models.Meal;
import com.visiontech.yummysmile.models.User;

import java.util.List;

/**
 * Interface to declare the methods to manipulate the main view.
 *
 * @author hetorres
 */
public interface MainView {
    /**
     * Show the progress dialog
     * @param show If the progress should be displayed or not
     */
    void showProgress(boolean show);

    /**
     * Shows a message to the user
     * @param message
     */
    void showMessage(String message);

    /**
     * Show the meals in the user interface
     * @param mealList
     */
    void showMealsItems(List<Meal> mealList);

    /**
     * Opens the login screen
     */
    void showLoginScreen();

    /**
     * Shows the user profile information in the drawer
     * @param user
     */
    void showUserInfo(User user);

    /**
     * Set if the pull to refresh should be display or not
     * @param refreshing If the pull to refresh should be display or not
     */
    void setRefreshing(boolean refreshing);
}
