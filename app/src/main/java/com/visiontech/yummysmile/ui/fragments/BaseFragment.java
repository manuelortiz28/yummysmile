package com.visiontech.yummysmile.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.common.base.Preconditions;
import com.visiontech.yummysmile.R;
import com.visiontech.yummysmile.YummySmileApplication;
import com.visiontech.yummysmile.di.components.ActivityPresenterComponent;
import com.visiontech.yummysmile.di.components.CoreComponent;
import com.visiontech.yummysmile.di.components.FragmentPresenterComponent;
import com.visiontech.yummysmile.di.scopes.PerFragment;
import com.visiontech.yummysmile.ui.activity.BaseActivity;
import com.visiontech.yummysmile.ui.presenter.BaseFragmentPresenter;
import com.visiontech.yummysmile.ui.presenter.view.fragment.BaseFragmentView;

/**
 * @author manuel.ortiz
 */
@PerFragment
public class BaseFragment extends Fragment implements BaseFragmentView {
    private static final String LOG_TAG = BaseFragment.class.getName();

    protected YummySmileApplication application;
    protected BaseFragmentPresenter basePresenter;
    protected CoreComponent coreComponent;
    protected ActivityPresenterComponent activityPresenterComponent;
    protected FragmentPresenterComponent fragmentPresenterComponent;
    protected BaseActivity baseActivity;

    private ProgressBar loader;
    private boolean loaderFlag;

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        if (savedInstance != null) {
            Log.d(LOG_TAG, "onCreate() - savedInstanceState");
            //FIXME Why are we using flag instead of loaderFlag??
            //boolean flag = savedInstance.getBoolean("loader");
            //FIXME This line is crashing NPE
            //loader.setVisibility(flag ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public void onActivityCreated(Bundle savedInstance) {
        super.onActivityCreated(savedInstance);
        Preconditions.checkState(getActivity() instanceof BaseActivity, "The activity should inherits from BaseActivity");

        baseActivity = (BaseActivity) getActivity();
        application = (YummySmileApplication) getActivity().getApplication();
        coreComponent = application.getCoreComponent();
        activityPresenterComponent = application.getActivityPresenterComponent(baseActivity);
        fragmentPresenterComponent = application.getFragmentPresenterComponent(this, activityPresenterComponent);
        basePresenter = fragmentPresenterComponent.getBaseFragmentPresenter();

        loader = (ProgressBar) getView().findViewById(R.id.progressBar);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(LOG_TAG, "onSaveInstanceState()");
        super.onSaveInstanceState(outState);
        outState.putBoolean("loader", loaderFlag);
    }

    @Override
    public void showProgress(boolean show) {
        loader.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        loaderFlag = show;
    }

    @Override
    public void showMessage(String message) {
        //TODO Remove the toast and show SnackBar / alert?
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
