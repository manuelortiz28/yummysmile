package com.visiontech.yummysmile.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.visiontech.yummysmile.R;
import com.visiontech.yummysmile.YummySmileApplication;
import com.visiontech.yummysmile.models.User;
import com.visiontech.yummysmile.ui.presenter.view.DrawerPresenter;
import com.visiontech.yummysmile.ui.presenter.view.activity.DrawerActivityView;

import javax.inject.Inject;

/**
 * @author manuel.ortiz
 */
public abstract class DrawerActivity extends BaseActivity implements DrawerActivityView {
    private static final String LOG_TAG = DrawerActivity.class.getName();

    private DrawerLayout drawerLayout;

    @Inject
    protected YummySmileApplication yummySmileApplication;
    @Inject
    protected DrawerPresenter drawerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate()");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        activityPresenterComponent.inject(this);

        setUpToolbar(R.string.header_time_line, R.id.app_bar, R.drawable.ic_menu_white_24dp);
        setUpNavDrawer();
    }

    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "onResume()");
        super.onResume();

        drawerPresenter.retrieveUserInformation();
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
                    drawerPresenter.logOutUser();
                } else {
                    //Fixme remove the message
                    showMessage("Item: " + menuItem.getTitle());
                }
                return true;
            }
        });
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



