package com.visiontech.yummysmile.accounts;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * @author manuel.ortiz
 *
 * Class to expose the account authentication as a Service. Using this the user will be able to add an account
 * from the accounts settings from the android operating system
 *
 */
public class YummyAuthenticatorService extends Service {
    private YummyAccountAuthenticator authenticator;

    @Override
    public void onCreate() {
        super.onCreate();
        authenticator = new YummyAccountAuthenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return authenticator.getIBinder();
    }
}
