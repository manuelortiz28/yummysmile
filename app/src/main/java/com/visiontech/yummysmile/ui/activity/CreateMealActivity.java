package com.visiontech.yummysmile.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Strings;
import com.google.gson.JsonObject;
import com.visiontech.yummysmile.R;
import com.visiontech.yummysmile.ui.presenter.CreateMealPresenter;
import com.visiontech.yummysmile.ui.presenter.CreateMealView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class to load the detail information about an specific meal
 *
 * @author hector.torres
 */
public class CreateMealActivity extends AppCompatActivity implements CreateMealView {

    private static final String LOG_TAG = CreateMealActivity.class.getName();
    private static final int REQUEST_IMAGE_CAPTURE = 100;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 200;
    private String currentPhotoPath;
    private CreateMealPresenter createMealPresenter;

    //UI
    private EditText mealName;
    private ImageView mealPicture;
    private File pictureToUpload;
    private ProgressBar progressBar;
    private TextInputLayout textWrappName;
    private TextInputLayout textWrappDescription;
    private EditText txtName;
    private EditText txtDescription;
    private TextView tvPictureLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_meal);

        setUpToolbar();

        mealName = (EditText) findViewById(R.id.et_name);
        mealPicture = (ImageView) findViewById(R.id.iv_meal_picture);
        progressBar = (ProgressBar) findViewById(R.id.pb_create_meal);

        txtName = (EditText) findViewById(R.id.et_name);
        textWrappName = (TextInputLayout) findViewById(R.id.ti_name_wrapper);

        txtDescription = (EditText) findViewById(R.id.et_description);
        textWrappDescription = (TextInputLayout) findViewById(R.id.ti_description_wrapper);

        tvPictureLabel = (TextView) findViewById(R.id.tv_title_img_source);

        mealPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissions();
            }
        });
    }

    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "onResume()");
        super.onResume();
        createMealPresenter = new CreateMealPresenter(this, CreateMealActivity.this);
    }

    @Override
    protected void onPause() {
        Log.d(LOG_TAG, "onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(LOG_TAG, "onStop()");
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_meal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.save_meal:
                if (Strings.isNullOrEmpty(txtName.getText().toString())) {
                    textWrappName.setError(getString(R.string.name_required_field));
                } else if (mealPicture.getDrawable() == null
                        || mealPicture.getTag().equals(getString(R.string.default_tag))) {
                    tvPictureLabel.setVisibility(View.VISIBLE);
                    tvPictureLabel.setError(tvPictureLabel.getText());
                } else {
                    // we have enough information to create the meal.
                    hideKeyboard();
                    showProgress(true);
                    createMealPresenter.createMeal(mealName.getText().toString(), pictureToUpload);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // ==============  Private methods ============

    private void setUpToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.tb_create_meal);
        toolbar.setTitle(getString(R.string.header_create_meal));
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Method that Ask for Runtime Permissions
     */
    private void requestPermissions() {
        Log.d(LOG_TAG, "1_requestPermissions()_");
        if (ContextCompat.checkSelfPermission(CreateMealActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(LOG_TAG, "_A)_");

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(CreateMealActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Log.d(LOG_TAG, "_Need explanation_");
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                Log.d(LOG_TAG, "_NO_Need explanation_");
                ActivityCompat.requestPermissions(CreateMealActivity.this,
                        new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_EXTERNAL_STORAGE);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            Log.d(LOG_TAG, "_D) We already have permissions_");
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
            String permissions[], int[] grantResults) {
        Log.d(LOG_TAG, "1.1_onRequestPermissionsResult_");
        switch (requestCode) {
            case REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.d(LOG_TAG, "2_Go_");
                    dispatchTakePictureIntent();
                } else {
                    // permission denied, Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    /**
     * Method that create the file after launch the intent to save the photo.
     */
    private void dispatchTakePictureIntent() {
        Log.d(LOG_TAG, "3_dispatchTakePictureIntent()_");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d(LOG_TAG, "IOException(): " + ex.getMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Log.d(LOG_TAG, "Launching CAMERA");
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    /**
     * Method that create the file to save on disk.
     */
    private File createImageFile() throws IOException {
        Log.d(LOG_TAG, "4_createImageFile()_");
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        Log.d(LOG_TAG, "createImageFile(): " + image.getAbsolutePath());
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * Result to load the image on the ImageView after taking the photo.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.d(LOG_TAG, "5_onActivityResult()_");
            //Get a thumbnail picture no full size.
            //            Bundle extras = data.getExtras();
            //            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //            mealPicture.setImageBitmap(imageBitmap);

            //Getting full size image
            setPicture();
        }
    }

    /**
     * Method that read the image from the device and then show it on the device.
     */
    private void setPicture() {
        Log.d(LOG_TAG, "6_setPicture()_");
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

        Log.d(LOG_TAG, "_currentPhotoPath:_" + currentPhotoPath);
        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);

        try {
            pictureToUpload = new File(currentPhotoPath);
            FileOutputStream fo = new FileOutputStream(pictureToUpload);
            fo.write(bos.toByteArray());
            fo.close();
        } catch (IOException e) {
            Log.d(LOG_TAG, "_IOException_Message" + e.getMessage());
            Log.d(LOG_TAG, "_IOException_Cause" + e.getCause());
            e.printStackTrace();
        }

        mealPicture.setImageBitmap(bitmap);
        mealPicture.setTag(currentPhotoPath);
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager
                    .HIDE_NOT_ALWAYS);
        }
    }


    // ==============  Presenter actions ============

    @Override
    public void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(CreateMealActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void createMealResponse(JsonObject jsonData) {
        Toast.makeText(CreateMealActivity.this, "Meal created successfully!", Toast.LENGTH_SHORT).show();
        showProgress(false);
        onBackPressed();
    }
}
