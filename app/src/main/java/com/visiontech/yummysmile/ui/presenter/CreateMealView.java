package com.visiontech.yummysmile.ui.presenter;

import com.google.gson.JsonObject;

/**
 * Interface to declare the methods to manipulate the create meal view.
 *
 * @author hetorres
 */
public interface CreateMealView {

    //TODO create an interface for a common usages like this method.
    void showProgress(boolean show);

    void showMessage(String message);

    void createMealResponse(JsonObject jsonObject);
}
