package com.visiontech.yummysmile.ui.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.visiontech.yummysmile.R;
import com.visiontech.yummysmile.models.Meal;
import com.visiontech.yummysmile.ui.adapter.MainCardsAdapter;
import com.visiontech.yummysmile.ui.presenter.HomePresenter;
import com.visiontech.yummysmile.ui.presenter.view.fragment.HomeFragmentView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author manuel.ortiz
 */
public class HomeFragment extends BaseFragment implements HomeFragmentView {

    protected HomePresenter homePresenter;

    private SwipeRefreshLayout swipeRefreshLayout;
    private MainCardsAdapter mainCardsAdapter;
    private View viewParent;

    private final MainCardsAdapter.MealCardOnClickListener mealCardOnClickListener = new MainCardsAdapter.MealCardOnClickListener() {
        @Override
        public void onMealCardClicked(Meal meal) {
            //TODO go to next screen
            Toast.makeText(getActivity(), "item: " + meal.getName(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle saveInstance) {
        viewParent = layoutInflater.inflate(R.layout.fragment_home_layout, null);

        setUpCardView();
        setUpSwipeRefresh();

        return viewParent;
    }

    @Override
    public void onActivityCreated(Bundle savedInstance) {
        super.onActivityCreated(savedInstance);

        homePresenter = fragmentPresenterComponent.getMainPresenter();

        basePresenter.validateUserLoggedIn();
    }

    @Override
    public void onResume() {
        super.onResume();

        showProgress(true);

        homePresenter.fetchMeals();
    }

    @Override
    public void showMealsItems(List<Meal> mealList) {
        mainCardsAdapter.clear();
        mainCardsAdapter.addAll(mealList);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        swipeRefreshLayout.setRefreshing(refreshing);
    }

    private void setUpSwipeRefresh() {
        swipeRefreshLayout = (SwipeRefreshLayout) viewParent.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                showProgress(true);
                homePresenter.fetchMeals();
            }
        });

        // Refreshing colors
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void setUpCardView() {
        RecyclerView recyclerView = (RecyclerView) viewParent.findViewById(R.id.main_cards_recycler_view);
        mainCardsAdapter = new MainCardsAdapter(getActivity(), new ArrayList<Meal>(), mealCardOnClickListener);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mainCardsAdapter);
        recyclerView.setHasFixedSize(true);
    }
}
