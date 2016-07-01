package com.visiontech.yummysmile.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.visiontech.yummysmile.YummySmileApplication;
import com.visiontech.yummysmile.di.components.ActivityPresenterComponent;
import com.visiontech.yummysmile.ui.presenter.view.activity.BaseActivityView;

import io.fabric.sdk.android.Fabric;

/**
 * @author manuel.ortiz
 *         <p/>
 *         Activity base, wrapping most of the activities functionalities
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseActivityView {

    protected YummySmileApplication application;
    protected ActivityPresenterComponent activityPresenterComponent;

    protected void onCreate(Bundle saveInstance) {
        super.onCreate(saveInstance);

        Fabric.with(this, new Crashlytics());

        application = (YummySmileApplication) getApplication();
        activityPresenterComponent = application.getActivityPresenterComponent(this);
    }

    @Override
    public void showLoginScreen() {
        startActivity(new Intent(this, AuthenticatorActivity.class));
        finish();
    }

    @Override
    public void showMessage(String message) {
        //TODO Remove the toast and show SnackBar / alert?
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setUpToolbar(@StringRes int title, @IdRes int toolbarViewId, @DrawableRes int icon) {
        final Toolbar toolbar = (Toolbar) findViewById(toolbarViewId);
        toolbar.setTitle(getString(title));
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(icon);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
