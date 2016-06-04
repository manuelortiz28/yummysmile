package com.visiontech.yummysmile.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.visiontech.yummysmile.R;
import com.visiontech.yummysmile.YummySmileApplication;
import com.visiontech.yummysmile.di.components.ActivityPresenterComponent;
import com.visiontech.yummysmile.models.User;
import com.visiontech.yummysmile.ui.presenter.LoginPresenter;
import com.visiontech.yummysmile.ui.presenter.view.activity.DrawerActivityView;
import com.visiontech.yummysmile.ui.presenter.view.activity.LoginActivityView;

import javax.inject.Inject;

import io.fabric.sdk.android.Fabric;

/**
 * @author manuel.ortiz
 */
public abstract class DrawerActivity extends BaseActivity implements DrawerActivityView, DrawerActivityMethods, LoginActivityView {
    private static final String LOG_TAG = HomeActivity.class.getName();

    private DrawerLayout drawerLayout;

    @Inject
    protected YummySmileApplication yummySmileApplication;
    @Inject
    protected LoginPresenter loginPresenter;

    protected ActivityPresenterComponent activityPresenterComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.activity_main);

        activityPresenterComponent = application.getActivityPresenterComponent(this);
        activityPresenterComponent.inject(this);

        setUpToolbar(R.string.header_time_line, R.id.app_bar, R.drawable.ic_menu_white_24dp);
        setUpNavDrawer();
    }

    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "onResume()");
        super.onResume();
    }

    @Override
    protected void onStop() {
        Log.d(LOG_TAG, "onStop()");
        super.onStop();
    }

    //===========================================================================================================
    //===============================================   Presenter actions    ====================================
    //===========================================================================================================
    @Override
    public void showLoginScreen() {
        startActivity(new Intent(this, AuthenticatorActivity.class));
        finish();
    }

    @Override
    public void showUserInfo(User user) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);

        if (navigationView.getHeaderCount() > 0 && user != null) {
            View headerView = navigationView.getHeaderView(0);
            ((TextView) headerView.findViewById(R.id.tv_name)).setText(user.getFullName());
            ((TextView) headerView.findViewById(R.id.tv_email)).setText(user.getEmail());
        }
    }

    //===========================================================================================================
    //===============================================   Private methods    ======================================
    //===========================================================================================================

    private void setUpNavDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);

        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                drawerLayout.closeDrawers();
                menuItem.setChecked(true);

                if (menuItem.getItemId() == R.id.nav_item_logout) {
                    loginPresenter.logoutUser();
                } else {
                    //Fixme remove the message
                    showMessage("Item: " + menuItem.getTitle());
                }
                return true;
            }
        });
    }

    @Override
    public ActivityPresenterComponent getActivityPresenterComponent() {
        return activityPresenterComponent;
    }

    @Override
    public void showError(String errorMessage) {
        //FIXME show the snake bar generic error message
        showMessage(errorMessage);
    }

    @Override
    public void showSuccess(User user) {
        showUserInfo(user);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            showMessage("Home");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}



