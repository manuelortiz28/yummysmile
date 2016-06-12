package com.visiontech.yummysmile.ui.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Strings;
import com.google.gson.JsonObject;
import com.visiontech.yummysmile.R;
import com.visiontech.yummysmile.di.components.ActivityPresenterComponent;
import com.visiontech.yummysmile.di.components.FragmentPresenterComponent;
import com.visiontech.yummysmile.ui.activity.BaseActivity;
import com.visiontech.yummysmile.ui.activity.PermissionsActivity;
import com.visiontech.yummysmile.ui.presenter.CreateMealPresenter;
import com.visiontech.yummysmile.ui.presenter.view.fragment.CreateMealFragmentView;
import com.visiontech.yummysmile.util.Constants;
import com.visiontech.yummysmile.util.PermissionsHelper;
import com.visiontech.yummysmile.util.UIHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Fragment to handle creation of a new meal.
 *
 * @author hetorres
 */
public class CreateMealFragment extends BaseFragment implements CreateMealFragmentView {
    private static final String TAG = CreateMealFragment.class.getName();
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 200;
    private static final int REQUEST_CAMERA = 0;
    private static final int SELECT_PHOTO = 1;
    private static final String[] PERMISSIONS = new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private EditText txtMealName;
    private ImageView mealPicture;
    private TextInputLayout textWrappName;
    private TextView tvPictureLabel;
    private CreateMealPresenter createMealPresenter;
    private EditText txtDescription;
    private ProgressBar progressBar;
    private File pictureToUpload;

    private PermissionsHelper permissionsHelper;
    private String optionSelected = "None";
    private String currentPhotoPath;

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
        textWrappName = (TextInputLayout) viewParent.findViewById(R.id.ti_name_wrapper);
        tvPictureLabel = (TextView) viewParent.findViewById(R.id.tv_title_img_source);
        txtDescription = (EditText) viewParent.findViewById(R.id.et_description);
        progressBar = (ProgressBar) viewParent.findViewById(R.id.progressBar);

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

        ActivityPresenterComponent activityPresenterComponent = application.getActivityPresenterComponent((BaseActivity) getActivity());
        FragmentPresenterComponent fragmentPresenterComponent = application.getFragmentPresenterComponent(this, activityPresenterComponent);
        createMealPresenter = fragmentPresenterComponent.getCreateMealPresenter();
        permissionsHelper = application.getCoreComponent().getPermissionsHelper();
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
    public void createMealResponse(JsonObject jsonObject) {
        Toast.makeText(getActivity(), "Meal created successfully!", Toast.LENGTH_SHORT).show();
        getActivity().onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            } else if (requestCode == SELECT_PHOTO) {
                onSelectFromGalleryResult(data);
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            //TODO do we need this?
        } else {
            if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
                getActivity().finish();
            } else {
                getImageIntent();
            }
        }
    }

    /**
     * ======== Private Methods ==========
     */

    private void saveMeal() {
        if (Strings.isNullOrEmpty(txtMealName.getText().toString())) {
            textWrappName.setError(getString(R.string.name_required_field));
        } else if (mealPicture.getDrawable() == null
                || getString(R.string.default_tag).equals(mealPicture.getTag()) || pictureToUpload == null) {
            tvPictureLabel.setVisibility(View.VISIBLE);
            tvPictureLabel.setError(tvPictureLabel.getText());
        } else {
            // we have enough information to create the meal.
            UIHelper.hideKeyboard(getActivity());
            createMealPresenter.createMeal(txtMealName.getText().toString(), pictureToUpload);
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                Log.d(TAG, "1_requestPermissions()_");
                optionSelected = items[item].toString();
                if (permissionsHelper.permissionsCheck(PERMISSIONS)) {
                    Log.d(TAG, "2 PermissionsActivity");
                    PermissionsActivity.startActivityForResult(getActivity(), REQUEST_WRITE_EXTERNAL_STORAGE, PERMISSIONS);
                } else {
                    Log.d(TAG, "3 getImageIntent");
                    getImageIntent();
                }
            }
        });
        builder.show();
    }

    private void getImageIntent() {
        if (optionSelected.equals("Take Photo")) {
            Log.d(TAG, "<< TAKE PHOTO");
            cameraIntent();
        } else if (optionSelected.equals("Choose from Library")) {
            Log.d(TAG, "<< FROM GALLERY");
            galleryIntent();
        }
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_PHOTO);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mealPicture.setImageBitmap(thumbnail);
        mealPicture.setTag("Camera");

        pictureToUpload = destination;
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ByteArrayOutputStream bytes2 = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes2);

        File destination2 = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination2.createNewFile();
            fo = new FileOutputStream(destination2);
            fo.write(bytes2.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mealPicture.setImageBitmap(bm);
        mealPicture.setTag("File");

        pictureToUpload = destination2;
    }

    //
    //        // Ensure that there's a camera activity to handle the intent
    //        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
    //            // Create the File where the photo should go
    //            File photoFile = null;
    //            try {
    //                photoFile = createImageFile();
    //            } catch (IOException ex) {
    //                // Error occurred while creating the File
    //                Log.d(TAG, "IOException(): " + ex.getMessage());
    //            }
    //            // Continue only if the File was successfully created
    //            if (photoFile != null) {
    //                Log.d(TAG, "Launching CAMERA");
    //                intent.putExtra(MediaStore.EXTRA_OUTPUT,
    //                        Uri.fromFile(photoFile));
    //                startActivityForResult(intent, REQUEST_CAMERA);
    //            }
    //        }
    //    }

    /**
     * Method that create the file to save on disk.
     */
    private File createImageFile() throws IOException {
        Log.d(TAG, "4_createImageFile()_");
        // Create an image file name
        String timeStamp = new SimpleDateFormat(Constants.PHOTO_TIME_FORMAT).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        Log.d(TAG, "createImageFile(): " + image.getAbsolutePath());
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * Method that read the image from the device and then show it on the device.
     */
    private void setPicture() {
        Log.d(TAG, "6_setPicture()_");
        // Get the dimensions of the View
        //        int targetW = mealPicture.getWidth();
        //Fixme figure out the best way to do it.
        int targetW = 400;
        int targetH = 400;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Log.d(TAG, "_currentPhotoPath:_" + currentPhotoPath);
        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);

        try {
            pictureToUpload = new File(currentPhotoPath);
            FileOutputStream fo = new FileOutputStream(pictureToUpload);
            fo.write(bos.toByteArray());
            fo.close();
        } catch (IOException e) {
            Log.d(TAG, "_IOException_Message" + e.getMessage());
            Log.d(TAG, "_IOException_Cause" + e.getCause());
            e.printStackTrace();
        }

        mealPicture.setImageBitmap(bitmap);
        mealPicture.setTag(currentPhotoPath);
    }
}
