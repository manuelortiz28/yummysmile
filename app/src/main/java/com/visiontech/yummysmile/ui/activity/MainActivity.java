package com.visiontech.yummysmile.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.squareup.otto.Subscribe;
import com.visiontech.yummysmile.R;
import com.visiontech.yummysmile.YummySmileApplication;
import com.visiontech.yummysmile.repository.api.dto.MealDTO;
import com.visiontech.yummysmile.repository.api.dto.MealsDTO;
import com.visiontech.yummysmile.ui.controller.MealsControllerImpl;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    Button btnRequest;
    MealsControllerImpl mealsController;
    private static final String TAG = "YummySmile :)";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        Toolbar toolbar = ((Toolbar) findViewById(R.id.app_bar));
        setSupportActionBar(toolbar);

        btnRequest = (Button) findViewById(R.id.btn_request);
        mealsController = new MealsControllerImpl(YummySmileApplication.getEventBus());
    }

    @Override
    protected void onResume() {
        super.onResume();

        YummySmileApplication.getEventBus().register(this);

        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mealsController.getMeals();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        YummySmileApplication.getEventBus().unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onFetchMeals(MealsControllerImpl.RetrieveMealsEvent event) {
        if (event.isSuccess() && event.getResponse() != null) {
            MealsDTO meals = event.getResponse();
            if (meals != null && !meals.getMeals().isEmpty()) {
                for (MealDTO meal : meals.getMeals()) {
                    Log.i("", meal.getName());
                    Log.i("", "" + meal.getPrice());
                    Toast.makeText(this, "Showing meals", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "All Cool, but no meals", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
