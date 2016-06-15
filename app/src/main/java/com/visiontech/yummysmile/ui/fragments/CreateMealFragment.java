package com.visiontech.yummysmile.ui.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
    private static final int DESIRE_SIZE = 600;
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

        // Permissions actions
        if (resultCode == PermissionsActivity.PERMISSIONS_GRANTED && requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            getImageIntent(); // we should arrive here in most cases when we already have permission.
        } else if (resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            getActivity().finish();
        }

        //  Response after get the picture (from amera or gallery)
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAMERA:
                    captureImageResult();
                    break;
                case SELECT_PHOTO:
                    selectFromGalleryResult(data);
                    break;
                default: break;
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
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
            Log.d(TAG, "IOException(): " + ex.getMessage());
        }

        // Continue only if the File was successfully created
        if (photoFile != null) {
            if (optionSelected.equals("Take Photo")) {
                Log.d(TAG, "<< TAKE PHOTO");
                cameraIntent(photoFile);
            } else if (optionSelected.equals("Choose from Library")) {
                Log.d(TAG, "<< FROM GALLERY");
                galleryIntent(photoFile);
            }
        }
    }

    private void galleryIntent(File file) {
        Intent pickGalleryIntent = new Intent(Intent.ACTION_PICK);
        pickGalleryIntent.setType("image/*");
        pickGalleryIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(Intent.createChooser(pickGalleryIntent, "Select File"), SELECT_PHOTO);
    }

    private void cameraIntent(File file) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(takePictureIntent, REQUEST_CAMERA);
    }

    private void captureImageResult() {
        Bitmap finalBitmap = resizeFileBitMap(currentPhotoPath);
        setFinalView(finalBitmap);
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
                if (picturePath != null) {
                    Bitmap finalBitmap = resizeFileBitMap(picturePath);
                    setFinalView(finalBitmap);
                } else {
                    Log.e(TAG, "We could not get the path of the file.");
                }
                cursor.close();
            }
        }
    }

    /**
     * Method that create the file to save on disk.
     */
    private File createImageFile() throws IOException {
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
     * Method that resize the image before showing in preview.
     */
    private Bitmap resizeFileBitMap(String filePath) {
        Log.d(TAG, "6_setPicture()_");
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / DESIRE_SIZE, photoH / DESIRE_SIZE);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Log.d(TAG, "_currentPhotoPath:_" + filePath);
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, bmOptions);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);

        try {
            pictureToUpload = new File(filePath);
            FileOutputStream fo = new FileOutputStream(pictureToUpload);
            fo.write(bos.toByteArray());
            fo.close();
        } catch (IOException e) {
            Log.d(TAG, "_IOException_Message" + e.getMessage());
            Log.d(TAG, "_IOException_Cause" + e.getCause());
            e.printStackTrace();
        }
        return bitmap;
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
