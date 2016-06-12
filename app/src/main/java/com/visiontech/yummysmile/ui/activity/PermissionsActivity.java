package com.visiontech.yummysmile.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import com.visiontech.yummysmile.R;
import com.visiontech.yummysmile.util.PermissionsHelper;

/**
 * Activity to handle the flow and logic for Permissions on Android M or above. Also handle permission declined contingency.
 * @author hetorres
 */
public class PermissionsActivity extends BaseActivity {

    public static final int PERMISSIONS_GRANTED = 0;
    public static final int PERMISSIONS_DENIED = 1;
    private static final String PERMISSIONS_KEY = "PERMISSIONS";
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final String PACKAGE = "package:";

    private PermissionsHelper permissionsHelper;
    private boolean requiresCheck;

    public static void startActivityForResult(Activity activity, int requestCode, String... permissions) {
        Intent intent = new Intent(activity, PermissionsActivity.class);
        intent.putExtra(PERMISSIONS_KEY, permissions);
        ActivityCompat.startActivityForResult(activity, intent, requestCode, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null || !getIntent().hasExtra(PERMISSIONS_KEY)) {
            throw new RuntimeException("This Activity needs to be launched using the static startActivityForResult() method.");
        }

        setContentView(R.layout.activity_permissions);
        permissionsHelper = application.getCoreComponent().getPermissionsHelper();
        requiresCheck = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        String[] permissions = getIntent().getStringArrayExtra(PERMISSIONS_KEY);

        if(requiresCheck){
            if (permissionsHelper.permissionsCheck(permissions)) {
                ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
            } else {
                allPermissionsGranted();
            }
        }else{
            requiresCheck = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && hasAllPermissionsGranted(grantResults)) {
            allPermissionsGranted();
            requiresCheck = true;
        } else {
            showMissingPermissionDialog();
            requiresCheck = false;
        }
    }


    /**
     * ======== Private Methods ==========
     */

    private void allPermissionsGranted() {
        setResult(PERMISSIONS_GRANTED);
        finish();
    }

    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Custom dialog to verify if the user wants to grant permissions or not.
     */
    private void showMissingPermissionDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(PermissionsActivity.this);
        dialogBuilder.setTitle("Why do we ask?");
        dialogBuilder.setMessage("We need your permission to access to your gallery and upload a photo, or take a photo from camera.");
        dialogBuilder.setNegativeButton("Denied", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setResult(PERMISSIONS_DENIED);
                finish();
            }
        });
        dialogBuilder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startAppSettings();
            }
        });
        dialogBuilder.show();
    }

    /**
     * Launch the settings screen to enable permissions.
     */
    private void startAppSettings() {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE + getPackageName()));
        startActivity(intent);
    }

}
