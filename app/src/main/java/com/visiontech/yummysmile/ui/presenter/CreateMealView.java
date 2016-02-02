package com.visiontech.yummysmile.ui.presenter;

import com.google.gson.JsonObject;

/**
 * Interface to declare the methods to manipulate the create meal view.
 *
 * @author hetorres
 */
public interface CreateMealView extends BaseView {
    void createMealResponse(JsonObject jsonObject);
}
