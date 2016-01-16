package com.visiontech.yummysmile.ui.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.visiontech.yummysmile.R;
import com.visiontech.yummysmile.ui.controller.MealsControllerImpl;

import java.io.File;

/**
 * Class that is the intermediary between the view and model based on MVP Pattern.
 *
 * @author hetorres
 */
public class CreateMealPresenter extends BasePresenter {
    private static final String LOG_TAG = CreateMealPresenter.class.getName();
    private MealsControllerImpl mealsController;
    private CreateMealView createMealView;
    private Context context;

    public CreateMealPresenter(CreateMealView createMealView, Context context) {
        this.createMealView = createMealView;
        this.context = context;
        this.mealsController = new MealsControllerImpl(this);
    }

    public void createMeal(String name, File photo) {
        Log.d(LOG_TAG, "createMeal()");
        createMealView.showProgress(true);

        //fixme create in a better way?
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", name);
        mealsController.createMeal(jsonObject, photo);
    }

    @Override
    public <T> void onNext(T data) {
        Log.d(LOG_TAG, "onNext()");
        JsonObject jsonResponse = (JsonObject) data;
        if (jsonResponse.has("objectId") && jsonResponse.get("objectId") != null) {
            String objectId = jsonResponse.get("objectId").getAsString();
            Log.d(LOG_TAG, "objectId: " + objectId);
        }

        if (jsonResponse.has("filename") && jsonResponse.get("filename") != null) {
            String filename = jsonResponse.get("filename").getAsString();
            Log.d(LOG_TAG, "filename: " + filename);
            createMealView.createMealResponse(jsonResponse);
        }
    }

    @Override
    public void onError(Throwable e) {
        Log.d(LOG_TAG, "onError()");
        //FIXME find the final copy.
        createMealView.showMessage(String.format(context.getString(R.string.general_error), e.getMessage()));
        Log.d(LOG_TAG, "Message: " + e.getMessage());
        Log.d(LOG_TAG, "Cause: " + e.getCause());
        //TODO Do we have to show other view? like some text on the layout?
    }
}
