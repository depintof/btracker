package com.innovamos.btracker.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by root on 29/05/16.
 */
public class BtrackerAuthenticatorService extends Service {
    private BtrackerAuthenticator authenticator;


    @Override
    public void onCreate() {
        authenticator = new BtrackerAuthenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return authenticator.getIBinder();
    }
}
