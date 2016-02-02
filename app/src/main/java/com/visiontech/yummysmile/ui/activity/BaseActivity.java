package com.visiontech.yummysmile.ui.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Base Class to handle the definitions for all the Activities.
 *
 * @author hector.torres
 */
public class BaseActivity extends AppCompatActivity {

    protected void setUpToolbar(int title, int layout, int icon) {
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
