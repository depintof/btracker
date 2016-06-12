package com.innovamos.btracker.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BTrackerSyncService extends Service{
    private static final String LOG_TAG = BTrackerSyncService.class.getSimpleName();

    private static final Object sSyncAdapterLock = new Object();
    private static BTrackerSyncAdapter syncAdapter = null;

    @Override
    public void onCreate() {
        Log.d(LOG_TAG, "onCreate - SunshineSyncService");
        synchronized (sSyncAdapterLock) {
            if (syncAdapter == null) {
                syncAdapter = new BTrackerSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return syncAdapter.getSyncAdapterBinder();
    }
}
