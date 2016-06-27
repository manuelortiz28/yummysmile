package com.visiontech.yummysmile.ui.activity;

import android.os.Bundle;
import android.util.Log;

import com.visiontech.yummysmile.R;
import com.visiontech.yummysmile.ui.fragments.CreateMealFragment;

/**
 * Activity to create a Meal.
 *
 * @author hetorres
 */
public class CreateMealActivity extends BaseActivity {

    private static final String LOG_TAG = CreateMealActivity.class.getName();

    @Override
    protected void onCreate(Bundle saveInstance) {
        Log.d(LOG_TAG, "onCreate()");
        super.onCreate(saveInstance);
        setContentView(R.layout.create_meal_activity);

        if (saveInstance == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_content_create_meal, new CreateMealFragment()).commit();
        }
    }
}
