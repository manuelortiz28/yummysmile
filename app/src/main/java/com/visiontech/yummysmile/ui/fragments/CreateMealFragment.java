package com.visiontech.yummysmile.ui.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Strings;
import com.visiontech.yummysmile.R;
import com.visiontech.yummysmile.di.components.FragmentPresenterComponent;
import com.visiontech.yummysmile.models.Meal;
import com.visiontech.yummysmile.ui.activity.BaseActivity;
import com.visiontech.yummysmile.ui.activity.PermissionsActivity;
import com.visiontech.yummysmile.ui.presenter.CreateMealPresenter;
import com.visiontech.yummysmile.ui.presenter.view.fragment.CreateMealFragmentView;
import com.visiontech.yummysmile.util.PermissionsHelper;
import com.visiontech.yummysmile.util.RenderImageHelper;
import com.visiontech.yummysmile.util.UIHelper;

import java.io.File;
import java.io.IOException;

/**
 * Fragment to handle creation of a new meal.
 *
 * @author hetorres
 */
public class CreateMealFragment extends BaseFragment implements CreateMealFragmentView {
    private static final String TAG = CreateMealFragment.class.getName();
    private static final String IMAGE_TYPE = "image/*";
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 200;
    private static final int REQUEST_CAMERA = 0;
    private static final int SELECT_PHOTO = 1;
    private static final String[] PERMISSIONS = new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private EditText txtMealName;
    private ImageView mealPicture;
    private TextInputLayout textWrapName;
    private TextView tvPictureLabel;

    private File pictureToUpload;
    private PhotoPicker optionSelected;
    private String currentPhotoPath;

    private CreateMealPresenter createMealPresenter;
    private PermissionsHelper permissionsHelper;
    private RenderImageHelper renderImageHelper;

    private enum PhotoPicker {
        TAKE_PHOTO,
        PICK_PHOTO;
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewParent = inflater.inflate(R.layout.fragment_create_meal_layout, container, false);

        txtMealName = (EditText) viewParent.findViewById(R.id.et_name);
        mealPicture = (ImageView) viewParent.findViewById(R.id.iv_meal_picture);
        textWrapName = (TextInputLayout) viewParent.findViewById(R.id.ti_name_wrapper);
        tvPictureLabel = (TextView) viewParent.findViewById(R.id.tv_title_img_source);
        mealPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        return viewParent;
    }

    @Override
    public void onActivityCreated(Bundle savedInstance) {
        super.onActivityCreated(savedInstance);
        ((BaseActivity) getActivity()).setUpToolbar(R.string.header_create_meal, R.id.tb_create_meal, R.drawable.ic_arrow_back_white_24dp);

        FragmentPresenterComponent fragmentPresenterComponent = application.getFragmentPresenterComponent(this, (BaseActivity) getActivity());
        createMealPresenter = fragmentPresenterComponent.getCreateMealPresenter();
        permissionsHelper = application.getCoreComponent().getPermissionsHelper();
        renderImageHelper = application.getCoreComponent().getRenderImageHelper();
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
                saveMeal();
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void createMealResponse(Meal meal) {
        Toast.makeText(getActivity(), "Meal created successfully!", Toast.LENGTH_SHORT).show();
        getActivity().onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Permissions actions
        if (resultCode == PermissionsActivity.PERMISSIONS_GRANTED && requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            getImageIntent(); // we should arrive here in most cases when we already have permission.
        } else if (resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            getActivity().finish();
        } else if (resultCode == Activity.RESULT_OK) { //  Response after get the picture (from camera or gallery)
            switch (requestCode) {
                case REQUEST_CAMERA:
                    captureImageResult();
                    break;

                case SELECT_PHOTO:
                    selectFromGalleryResult(data);
                    break;

                default:
                    break;
            }
        }
    }


    /**
     * ======== Private Methods ==========
     */

    private void saveMeal() {
        if (Strings.isNullOrEmpty(txtMealName.getText().toString())) {
            textWrapName.setError(getString(R.string.name_required_field));
        } else if (mealPicture.getDrawable() == null
                || getString(R.string.default_tag_photo).equals(mealPicture.getTag()) || pictureToUpload == null) {
            tvPictureLabel.setVisibility(View.VISIBLE);
            tvPictureLabel.setError(tvPictureLabel.getText());
        } else {
            // we have enough information to create the meal.
            UIHelper.hideKeyboard(getActivity());
            createMealPresenter.createMeal(txtMealName.getText().toString(), pictureToUpload);
        }
    }

    private void selectImage() {
        final CharSequence[] items = {getString(R.string.take_photo), getString(R.string.choose_photo)};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.alert_photo_title);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                Log.d(TAG, "1_requestPermissions()_");
                optionSelected = PhotoPicker.values()[item];
                if (permissionsHelper.permissionsCheck(PERMISSIONS)) {
                    Log.d(TAG, "2 PermissionsActivity");
                    callPermissionsActivity();
                } else {
                    Log.d(TAG, "3 getImageIntent");
                    getImageIntent();
                }
            }
        });
        builder.show();
    }

    private void getImageIntent() {
        // Create the File where the photo should go
        File photoFile = null;
        try {
            photoFile = renderImageHelper.createImageFile();
            currentPhotoPath = photoFile.getAbsolutePath();
            if (optionSelected == PhotoPicker.TAKE_PHOTO) {
                Log.d(TAG, "<< TAKE PHOTO");
                launchCameraIntent(photoFile);
            } else if (optionSelected == PhotoPicker.PICK_PHOTO) {
                Log.d(TAG, "<< FROM GALLERY");
                launchGalleryIntent(photoFile);
            }
        } catch (IOException ex) {
            Log.d(TAG, "IOException(Error occurred while creating the File): " + ex.getMessage());
        }
    }

    private void launchGalleryIntent(File file) {
        Intent pickGalleryIntent = new Intent(Intent.ACTION_PICK);
        pickGalleryIntent.setType(IMAGE_TYPE);
        pickGalleryIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(Intent.createChooser(pickGalleryIntent, getString(R.string.select_file)), SELECT_PHOTO);
    }

    private void launchCameraIntent(File file) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(takePictureIntent, REQUEST_CAMERA);
    }

    private void captureImageResult() {
        if (renderImageHelper.resizeFileBitMap(currentPhotoPath)) {
            setFinalView(renderImageHelper.getRenderedBitmap());
            pictureToUpload = renderImageHelper.getImageToUpload();
        }
    }

    private void selectFromGalleryResult(Intent data) {
        if (data != null) {
            Uri selectedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePath[0]);
                String picturePath = cursor.getString(columnIndex);
                if (picturePath != null && renderImageHelper.resizeFileBitMap(picturePath)) {
                    setFinalView(renderImageHelper.getRenderedBitmap());
                    pictureToUpload = renderImageHelper.getImageToUpload();
                }
                cursor.close();
            } else {
                Log.e(TAG, "We could not get the path of the file.");
            }
        }
    }

    private void setFinalView(Bitmap finalBitMap) {
        mealPicture.setImageBitmap(finalBitMap);
        mealPicture.setTag(currentPhotoPath);
    }

    private void callPermissionsActivity() {
        Intent intent = new Intent(getActivity(), PermissionsActivity.class);
        intent.putExtra(PermissionsActivity.PERMISSIONS_KEY, PERMISSIONS);
        startActivityForResult(intent, REQUEST_WRITE_EXTERNAL_STORAGE);
    }
}
