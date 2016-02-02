package com.visiontech.yummysmile.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.visiontech.yummysmile.R;
import com.visiontech.yummysmile.repository.api.dto.MealDTO;
import com.visiontech.yummysmile.repository.api.dto.MealsDTO;
import com.visiontech.yummysmile.ui.adapter.MainCardsAdapter;
import com.visiontech.yummysmile.ui.presenter.MainPresenter;
import com.visiontech.yummysmile.ui.presenter.MainView;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;

/**
 * Class to load the landing screen (lists of meals).
 *
 * @author hector.torres
 */
public class MainActivity extends AppCompatActivity implements MainView {

    private static final String LOG_TAG = MainActivity.class.getName();
    private ProgressBar loader;
    private boolean loaderFlag;
    private MainPresenter mainPresenter;

    private DrawerLayout drawerLayout;
    private MainCardsAdapter mainCardsAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton fabButton;
    private final MainCardsAdapter.MealCardOnClickListener mealCardOnClickListener = new MainCardsAdapter.MealCardOnClickListener() {
        @Override
        public void onMealCardClicked(MealDTO mealDTO) {
            //TODO go to next screen
            Toast.makeText(MainActivity.this, "item: " + mealDTO.getName(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        setUpToolbar();
        setUpNavDrawer();
        setUpSwipeRefresh();
        setUpCardView();
        setUpFabButton();

        loader = (ProgressBar) findViewById(R.id.progressBar);

        if (savedInstanceState != null) {
            Log.d(LOG_TAG, "onCreate() - savedInstanceState");
            boolean flag = savedInstanceState.getBoolean("loader");
            loader.setVisibility(flag ? View.VISIBLE : View.INVISIBLE);
        }
    }

    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "onResume()");
        super.onResume();

        showProgress(true);
        mainPresenter = new MainPresenter(this, MainActivity.this);
        mainPresenter.fetchMeals();
    }

    @Override
    protected void onStop() {
        Log.d(LOG_TAG, "onStop()");
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
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                showMessage("Home");
                return true;
            case R.id.action_settings:
                showMessage("Settings");
                return true;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(LOG_TAG, "onSaveInstanceState()");
        super.onSaveInstanceState(outState);
        outState.putBoolean("loader", loaderFlag);
    }


    //===========================================================================================================
    //===============================================   Presenter actions    ====================================
    //===========================================================================================================

    @Override
    public void showProgress(boolean show) {
        loader.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        loaderFlag = show;
    }

    @Override
    public void showMessage(String message) {
        //TODO Remove the toast and show SnackBar / alert?
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void mealsItems(MealsDTO mealsDTO) {
        mainCardsAdapter.clear();
        mainCardsAdapter.addAll(mealsDTO.getMeals());
        swipeRefreshLayout.setRefreshing(false);
    }

    //===========================================================================================================
    //===============================================   Private methods    ======================================
    //===========================================================================================================

    private void setUpToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle(getString(R.string.header_time_line));
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setUpNavDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Fixme remove the toast
                Toast.makeText(MainActivity.this, "Item: " + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private void setUpSwipeRefresh() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                showProgress(true);
                mainPresenter.fetchMeals();
            }
        });

        // Refreshing colors
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void setUpCardView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mainCardsAdapter = new MainCardsAdapter(MainActivity.this, new ArrayList<MealDTO>(), mealCardOnClickListener);

        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mainCardsAdapter);
        recyclerView.setHasFixedSize(true);
    }

    private void setUpFabButton() {
        fabButton = (FloatingActionButton) findViewById(R.id.fab_button);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateMealActivity.class);
                startActivity(intent);
            }
        });
    }
}
