package com.visiontech.yummysmile.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Class in charge to handle the Permissions on the app.
 *
 * @author hector.torres
 */
public final class PermissionsHelper {

    private Context context;

    public PermissionsHelper(Context context) {
        this.context = context;
    }

    public boolean permissionsCheck(String... permissions) {
        for (String permission : permissions) {
            if (hasPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasPermission(String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }
}
