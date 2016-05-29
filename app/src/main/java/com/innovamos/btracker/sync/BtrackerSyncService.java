package com.innovamos.btracker.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by root on 29/05/16.
 */
public class BtrackerSyncService extends Service{
    private static final String LOG_TAG = "";

    private static final Object sSyncAdapterLock = new Object();
    private static BtrackerSyncAdapter syncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("SunshineSyncService", "onCreate - SunshineSyncService");
        synchronized (sSyncAdapterLock) {
            if (syncAdapter == null) {
                syncAdapter = new BtrackerSyncAdapter (getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return syncAdapter.getSyncAdapterBinder();
    }
}
