package com.visiontech.yummysmile.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
}
