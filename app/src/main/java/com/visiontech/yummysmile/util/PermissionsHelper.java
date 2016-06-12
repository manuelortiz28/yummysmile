package com.visiontech.yummysmile.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Utility class to verify if we have permissions or not to an specific permission.
 *
 * @author hector.torres
 */
@Singleton
public final class PermissionsHelper {

    private Context context;

    @Inject
    public PermissionsHelper(Context context) {
        this.context = context;
    }

    public boolean permissionsCheck(String... permissions) {
        for (String permission : permissions) {
            if (needPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    private boolean needPermission(String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED;
    }
}