package com.visiontech.yummysmile.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import com.google.common.base.Preconditions;
import com.visiontech.yummysmile.R;
import com.visiontech.yummysmile.util.PermissionsHelper;

/**
 * Activity to handle the flow and logic for Permissions on Android M or above. Also handle permission declined contingency.
 *
 * @author hetorres
 */
public class PermissionsActivity extends BaseActivity {
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final String PACKAGE = "package:";

    public static final int PERMISSIONS_GRANTED = 0;
    public static final int PERMISSIONS_DENIED = 1;
    public static final String PERMISSIONS_KEY = "PERMISSIONS";

    private PermissionsHelper permissionsHelper;
    private boolean requiresCheck = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Preconditions.checkNotNull(getIntent(), "This Activity needs to have PERMISSIONS_KEY parameter.");
        Preconditions.checkArgument(getIntent().hasExtra(PERMISSIONS_KEY), "This Activity needs to have PERMISSIONS_KEY parameter.");

        setContentView(R.layout.activity_permissions);
        permissionsHelper = application.getCoreComponent().getPermissionsHelper();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String[] permissions = getIntent().getStringArrayExtra(PERMISSIONS_KEY);

        if (requiresCheck) {
            if (permissionsHelper.permissionsCheck(permissions)) {
                ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
            } else {
                allPermissionsGranted();
            }
        } else {
            requiresCheck = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int... grantResults) {
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

    private boolean hasAllPermissionsGranted(@NonNull int... grantResults) {
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
        dialogBuilder.setTitle(R.string.permissions_title);
        dialogBuilder.setMessage(R.string.permissions_message);
        dialogBuilder.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setResult(PERMISSIONS_DENIED);
                finish();
            }
        });
        dialogBuilder.setPositiveButton(R.string.menu_action_settings, new DialogInterface.OnClickListener() {
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
