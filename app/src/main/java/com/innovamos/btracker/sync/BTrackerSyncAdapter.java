package com.innovamos.btracker.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.innovamos.btracker.R;
import com.innovamos.btracker.data.BtrackerContract;
import com.innovamos.btracker.utils.Cons;
import com.innovamos.btracker.web.DatabaseConnectivity;
import com.innovamos.btracker.web.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

public class BTrackerSyncAdapter extends AbstractThreadedSyncAdapter {
    public static final String LOG_TAG = BTrackerSyncAdapter.class.getSimpleName();

    ContentResolver resolver;

    // Interval at which to sync with the weather, in seconds.
    // 60 seconds (1 minute) * 180 = 3 hours
    //public static final int SYNC_INTERVAL = 60 * 60 * 3;
    public static final int SYNC_INTERVAL = 60;

    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;

    public BTrackerSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        resolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "onPerformSync Called.");
        boolean synchronizeLocally = extras.getBoolean(ContentResolver.SYNC_EXTRAS_UPLOAD, false);

        if (synchronizeLocally) {
            performLocalSync();
        }
        else {
            performRemoteSync();
        }
    }

    /**
     * Download data from remote database
     */
    private void performLocalSync() {
        Log.i(LOG_TAG, "Updating the client database");

    }

    /**
     * Synchronize with remote database
     */
    private void performRemoteSync() {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String beaconsJsonString;

        try {
            Uri builtUri = Uri.parse(Cons.GET_ALL_BEACONS);

            URL url = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();

            if (inputStream == null) {
                return;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
                buffer.append("\n");
            }

            if (buffer.length() == 0) {
                return;
            }

            beaconsJsonString = buffer.toString();

            Log.v(LOG_TAG, "+++ Retrieved string: " + beaconsJsonString);
            getBeaconsDataFromJSON(beaconsJsonString);
            //getWeatherDataFromJson(forecastJsonStr, locationQuery); // pending
        } catch (IOException e) {
            Log.e(LOG_TAG, " +++ Error ", e);
        } catch (Exception e) { // TODO: JSONException
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
    }

    private void getBeaconsDataFromJSON(String beaconsJSONString){
        final String WS_BEACONS = "beacons";
        final String WS_ID = "id";
        final String WS_UUID = "uuid";
        final String WS_MAJOR = "major";
        final String WS_NAME = "name";
        final String WS_MINOR = "minor";
        final String WS_DETECTION_RANGE = "detection_range";
        final String WS_CREATED = "created";
        final String WS_MODIFIED = "modified";

        try {
            JSONObject beaconsJSON = new JSONObject(beaconsJSONString);
            JSONArray beaconsArray = beaconsJSON.getJSONArray(WS_BEACONS);

            Vector<ContentValues> dataVector = new Vector<>(beaconsArray.length());


            for (int i = 0; i < beaconsArray.length(); i++){
                int id;
                String uuid;
                int major;
                String name;
                int minor;
                int detectionRange;
                String created;
                String modified;

                JSONObject singleBeacon = beaconsArray.getJSONObject(i);

                id = singleBeacon.getInt(WS_ID);
                uuid = singleBeacon.getString(WS_UUID);
                major = singleBeacon.getInt(WS_MAJOR);
                name = singleBeacon.getString(WS_NAME);
                minor = singleBeacon.getInt(WS_MINOR);
                detectionRange = singleBeacon.getInt(WS_DETECTION_RANGE);
                created = singleBeacon.getString(WS_CREATED);
                modified = singleBeacon.getString(WS_MODIFIED);

                ContentValues beaconValues = new ContentValues();
                beaconValues.put(BtrackerContract.BeaconsEntry.COLUMN_ID, id);
                beaconValues.put(BtrackerContract.BeaconsEntry.COLUMN_UUID, uuid);
                beaconValues.put(BtrackerContract.BeaconsEntry.COLUMN_MAJOR, major);
                beaconValues.put(BtrackerContract.BeaconsEntry.COLUMN_NAME, name);
                beaconValues.put(BtrackerContract.BeaconsEntry.COLUMN_MINOR, minor);
                beaconValues.put(BtrackerContract.BeaconsEntry.COLUMN_DETECTION_RANGE, detectionRange);
                beaconValues.put(BtrackerContract.BeaconsEntry.COLUMN_CREATED, created);
                beaconValues.put(BtrackerContract.BeaconsEntry.COLUMN_MODIFIED, modified);

                dataVector.add(beaconValues);
            }

            int inserted = 0;

            if ( dataVector.size() > 0 ) {
                ContentValues[] dataArray = new ContentValues[dataVector.size()];
                dataVector.toArray(dataArray);
                getContext().getContentResolver().bulkInsert(BtrackerContract.BeaconsEntry.CONTENT_URI, dataArray);

                Cursor cursor = getContext().getContentResolver().query(BtrackerContract.BeaconsEntry.CONTENT_URI, null, null, null, null);

                Log.v(LOG_TAG, "+++ Cursor: " + cursor.getCount());
                notifyApplication();
            }

            Log.d(LOG_TAG, "+++ Sync Complete. " + dataVector.size() + " Inserted");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void notifyApplication(){
        //TODO: Actions to notify application if necessary
    }

    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        if ( null == accountManager.getPassword(newAccount) ) {

            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            onAccountCreated(newAccount, context);

        }
        return newAccount;
    }

    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        Log.v(LOG_TAG, " +++ +++ account created");

        BTrackerSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

}
