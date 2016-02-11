package com.visiontech.yummysmile.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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
}
