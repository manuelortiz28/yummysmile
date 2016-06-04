package com.visiontech.yummysmile.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.visiontech.yummysmile.R;
import com.visiontech.yummysmile.ui.fragments.HomeFragment;

/**
 * Class to load the landing screen (lists of meals).
 *
 * @author hector.torres
 */
public class HomeActivity extends DrawerActivity {

    private static final String LOG_TAG = HomeActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate()");
        super.onCreate(savedInstanceState);

        setUpFabButton();

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.frame_content_layout, new HomeFragment()).commit();
        }
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
        if (item.getItemId() == R.id.action_settings) {
            showMessage("Settings");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpFabButton() {
        findViewById(R.id.fab_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CreateMealActivity.class);
                startActivity(intent);
            }
        });
    }
}
