package com.innovamos.btracker.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by root on 29/05/16.
 */
public class BtrackerProvider extends ContentProvider {
    private static final String LOG_TAG = BtrackerProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    static final int BEACONS = 100;

    private BtrackerDBHelper openHelper;

    @Override
    public boolean onCreate() {
        openHelper = new BtrackerDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor = null;


        switch (sUriMatcher.match(uri)) {
            case BEACONS:
                Log.v("Tag", " --- Making beacons query");
                retCursor = openHelper.getReadableDatabase().query(
                        BtrackerContract.BeaconsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            //TODO: add cases for other tables
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case BEACONS:
                return BtrackerContract.BeaconsEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = openHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri = null;


        switch (match) {
            case BEACONS:

                long _id = db.insert(BtrackerContract.BeaconsEntry.TABLE_NAME, null, values);

                if (_id > 0)
                    returnUri = BtrackerContract.BeaconsEntry.buildBeaconsUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        db.close();

        return returnUri;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = openHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case BEACONS:
                db.beginTransaction();
                int returnCount = 0;

                for (ContentValues value : values) {
                    try {
                        long _id = db.insertOrThrow(BtrackerContract.BeaconsEntry.TABLE_NAME, null, value);

                        if (_id != -1) {
                            returnCount++;
                        }
                    } catch (SQLException e) {
                        Log.v(LOG_TAG, " --- Insert on bulk failed");
                        continue;
                    }
                }
                db.setTransactionSuccessful();


                db.endTransaction();

                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = BtrackerContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, BtrackerContract.PATH_BEACONS, BEACONS);

        //TODO: add matcher for every URI

        return matcher;
    }
}
