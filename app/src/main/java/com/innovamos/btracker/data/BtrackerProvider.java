package com.innovamos.btracker.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by root on 29/05/16.
 */
public class BtrackerProvider extends ContentProvider {
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
        Cursor retCursor;

        switch (sUriMatcher.match(uri)){
            case BEACONS:
                openHelper.getReadableDatabase().query(
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

        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match){
            case BEACONS:
                return BtrackerContract.BeaconsEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = BtrackerContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, BtrackerContract.PATH_BEACONS, BEACONS);

        //TODO: add matcher for every URI

        return matcher;
    }
}
