package com.visiontech.yummysmile.ui.activity;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.visiontech.yummysmile.YummySmileApplication;

/**
 * @author manuel.ortiz
 *         <p/>
 *         Activity base, wrapping most of the activities functionalities
 */
public class BaseActivity extends AppCompatActivity {

    protected YummySmileApplication application;

    protected void onCreate(Bundle saveInstance) {
        super.onCreate(saveInstance);

        application = (YummySmileApplication) getApplication();
    }

    public void showMessage(String message) {
        //TODO Remove the toast and show SnackBar / alert?
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void setUpToolbar(@StringRes int title, @IdRes int layout, @DrawableRes int icon) {
        final Toolbar toolbar = (Toolbar) findViewById(layout);
        toolbar.setTitle(getString(title));
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(icon);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
