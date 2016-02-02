package com.visiontech.yummysmile.ui.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.visiontech.yummysmile.R;
import com.visiontech.yummysmile.ui.controller.MealsControllerImpl;
import com.visiontech.yummysmile.ui.subscriber.ResultListener;

import java.io.File;

/**
 * Class that is the intermediary between the view and model based on MVP Pattern.
 *
 * @author hetorres
 */
public class CreateMealPresenter {
    private static final String LOG_TAG = CreateMealPresenter.class.getName();
    private static final String JSON_NAME = "name";

    private MealsControllerImpl mealsController;
    private CreateMealView createMealView;
    private Context context;

    public CreateMealPresenter(CreateMealView createMealView, Context context) {
        this.createMealView = createMealView;
        this.context = context;
        this.mealsController = new MealsControllerImpl();
    }

    public void createMeal(String name, File photo) {
        Log.d(LOG_TAG, "createMeal()");
        createMealView.showProgress(true);

        //fixme create in a better way?
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(JSON_NAME, name);

        mealsController.createMeal(jsonObject, photo, new ResultListener<MealsControllerImpl.CreateMealResponse>() {
            @Override
            public void onResult(MealsControllerImpl.CreateMealResponse result) {
                if (result.isSuccess()) {
                    JsonObject jsonResponse = (JsonObject) result.getPayload();
                    createMealView.createMealResponse(jsonResponse);

                    //TODO remove this, this is just for debug purposes.
                    if (jsonResponse.has("objectId") && jsonResponse.get("objectId") != null) {
                        String objectId = jsonResponse.get("objectId").getAsString();
                        Log.d(LOG_TAG, "objectId: " + objectId);
                    }

                    if (jsonResponse.has("filename") && jsonResponse.get("filename") != null) {
                        String filename = jsonResponse.get("filename").getAsString();
                        Log.d(LOG_TAG, "filename: " + filename);
                    }
                } else {
                    //TODO Do we have to show other view? like some text on the layout?
                    Throwable e = result.getError();
                    createMealView.showMessage(String.format(context.getString(R.string.general_error), e.getMessage()));
                    Log.d(LOG_TAG, "Message: " + e.getMessage());
                    Log.d(LOG_TAG, "Cause: " + e.getCause());
                }
                createMealView.showProgress(false);
            }
        });
    }
}
