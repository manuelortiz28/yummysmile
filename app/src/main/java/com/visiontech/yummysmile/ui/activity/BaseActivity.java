package com.visiontech.yummysmile.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.visiontech.yummysmile.R;
import com.visiontech.yummysmile.YummySmileApplication;
import com.visiontech.yummysmile.ui.presenter.view.activity.BaseActivityView;

/**
 * @author manuel.ortiz
 *         <p/>
 *         Activity base, wrapping most of the activities functionalities
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseActivityView {

    protected YummySmileApplication application;

    protected void onCreate(Bundle saveInstance) {
        super.onCreate(saveInstance);

        application = (YummySmileApplication) getApplication();
    }

    @Override
    public void showProgress(boolean show) {
        View loader = findViewById(R.id.progressBar);

        if (loader != null) {
            loader.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        }
    }

    @Override
    public void showMessage(String message) {
        //TODO Remove the toast and show SnackBar / alert?
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
