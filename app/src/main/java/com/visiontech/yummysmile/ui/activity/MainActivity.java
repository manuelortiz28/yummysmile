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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.visiontech.yummysmile.R;
import com.visiontech.yummysmile.repository.api.dto.MealDTO;
import com.visiontech.yummysmile.repository.api.dto.MealsDTO;
import com.visiontech.yummysmile.ui.controller.MealsControllerImpl;
import com.visiontech.yummysmile.ui.presenter.MainPresenter;
import com.visiontech.yummysmile.ui.presenter.MainView;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements MainView {

    private Button btnRequest;
    private ProgressBar loader;
    private boolean loaderFlag;
    private MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("", "onCreate()");
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        Toolbar toolbar = ((Toolbar) findViewById(R.id.app_bar));
        setSupportActionBar(toolbar);

        btnRequest = (Button) findViewById(R.id.btn_request);
        loader = (ProgressBar) findViewById(R.id.progressBar);
        mainPresenter = new MainPresenter(this);

        if (savedInstanceState != null) {
            Log.d("", "onCreate() - savedInstanceState");
            boolean flag = savedInstanceState.getBoolean("loader");
            loader.setVisibility(flag ? View.VISIBLE : View.INVISIBLE);
        }

        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(true);
                mainPresenter.fetchMeals();
            }
        });
    }

    @Override
    protected void onResume() {
        Log.d("", "onResume()");
        super.onResume();
    }

    @Override
    protected void onStop() {
        Log.d("", "onStop()");
        super.onStop();
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d("", "onSaveInstanceState()");
        super.onSaveInstanceState(outState);
        outState.putBoolean("loader", loaderFlag);
    }


    // ==============  Presenter methods ============

    @Override
    public void showProgress(boolean show) {
        if (show) {
            loader.setVisibility(View.VISIBLE);
            loaderFlag = true;
        } else {
            loader.setVisibility(View.INVISIBLE);
            loaderFlag = false;
        }
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void mealsItems(MealsDTO mealsDTO) {
        for (MealDTO meal : mealsDTO.getMeals()) {
            Log.d("", meal.getName());
        }
        showMessage("Showing Elements");
    }
}
