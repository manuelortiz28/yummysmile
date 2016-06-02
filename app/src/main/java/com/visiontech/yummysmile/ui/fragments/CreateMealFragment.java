package com.visiontech.yummysmile.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Strings;
import com.google.gson.JsonObject;
import com.visiontech.yummysmile.R;
import com.visiontech.yummysmile.di.components.ActivityPresenterComponent;
import com.visiontech.yummysmile.di.components.FragmentPresenterComponent;
import com.visiontech.yummysmile.ui.activity.BaseActivity;
import com.visiontech.yummysmile.ui.presenter.CreateMealPresenter;
import com.visiontech.yummysmile.ui.presenter.view.fragment.CreateMealFragmentView;
import com.visiontech.yummysmile.util.UIHelper;

import java.io.File;

import javax.inject.Inject;

/**
 * Fragment to handle creation of a new meal.
 *
 * @author hetorres
 */
public class CreateMealFragment extends BaseFragment implements CreateMealFragmentView {
    private static final String LOG_TAG = CreateMealFragment.class.getName();

    private String currentPhotoPath;
    private static final int REQUEST_IMAGE_CAPTURE = 100;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 200;

    private View viewParent;
    private EditText txtMealName;
    private EditText txtDescription;
    private ImageView mealPicture;
    private File pictureToUpload;
    private ProgressBar progressBar;
    private TextInputLayout textWrappName;
    private TextInputLayout textWrappDescription;
    private TextView tvPictureLabel;

    @Inject
    protected CreateMealPresenter createMealPresenter;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewParent = inflater.inflate(R.layout.fragment_create_meal_layout, container, false);

        txtMealName = (EditText) viewParent.findViewById(R.id.et_name);
        txtDescription = (EditText) viewParent.findViewById(R.id.et_description);
        mealPicture = (ImageView) viewParent.findViewById(R.id.iv_meal_picture);
        progressBar = (ProgressBar) viewParent.findViewById(R.id.progressBar);
        textWrappName = (TextInputLayout) viewParent.findViewById(R.id.ti_name_wrapper);
        textWrappDescription = (TextInputLayout) viewParent.findViewById(R.id.ti_description_wrapper);
        tvPictureLabel = (TextView) viewParent.findViewById(R.id.tv_title_img_source);

        mealPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO implement this functionality.
            }
        });

        return viewParent;
    }

    @Override
    public void onActivityCreated(Bundle savedInstance) {
        super.onActivityCreated(savedInstance);
        ((BaseActivity) getActivity()).setUpToolbar(R.string.header_create_meal, R.id.tb_create_meal, R.drawable.ic_arrow_back_white_24dp);

        ActivityPresenterComponent activityPresenterComponent = application.getActivityPresenterComponent((BaseActivity) getActivity());
        FragmentPresenterComponent fragmentPresenterComponent = application.getFragmentPresenterComponent(this, activityPresenterComponent);
        createMealPresenter = fragmentPresenterComponent.getCreateMealPresenter();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_create_meal, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                getActivity().onBackPressed();
                return true;

            case R.id.save_meal:
                if (Strings.isNullOrEmpty(txtMealName.getText().toString())) {
                    textWrappName.setError(getString(R.string.name_required_field));
                } else if (mealPicture.getDrawable() == null
                        || mealPicture.getTag().equals(getString(R.string.default_tag))) {
                    tvPictureLabel.setVisibility(View.VISIBLE);
                    tvPictureLabel.setError(tvPictureLabel.getText());
                } else {
                    // we have enough information to create the meal.
                    UIHelper.hideKeyboard(getActivity());
                    createMealPresenter.createMeal(txtMealName.getText().toString(), pictureToUpload);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void createMealResponse(JsonObject jsonObject) {
        Toast.makeText(getActivity(), "Meal created successfully!", Toast.LENGTH_SHORT).show();
        getActivity().onBackPressed();
    }
}
