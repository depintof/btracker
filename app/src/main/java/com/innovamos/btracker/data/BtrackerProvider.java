package com.innovamos.btracker.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class BtrackerProvider extends ContentProvider {
    private static final String LOG_TAG = BtrackerProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final int BEACONS = 100;
    private static final int ITEM_BEACONS = 110;
    private static final int PRODUCTS = 200;
    private static final int ITEM_PRODUCTS = 210;
    private static final int PRODUCTS_ZONES = 300;
    private static final int ITEM_PRODUCTS_ZONES = 310;
    private static final int CUSTOMERS = 400;
    private static final int ITEM_CUSTOMERS = 410;
    private static final int CUSTOMERS_PRODUCTS = 500;
    private static final int ITEM_CUSTOMERS_PRODUCTS = 510;
    private static final int PURCHASES = 600;
    private static final int ITEM_PURCHASES = 610;
    private static final int VISITS = 700;
    private static final int ITEM_VISITS = 710;
    private static final int STORES = 800;
    private static final int ITEM_STORES = 810;
    private static final int ZONES = 900;
    private static final int ITEM_ZONES = 910;

    private BtrackerDBHelper openHelper;
    private ContentResolver resolver;

    @Override
    public boolean onCreate() {
        openHelper = new BtrackerDBHelper(getContext());
        resolver = getContext().getContentResolver();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        SQLiteDatabase db = openHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case BEACONS:
                Log.v("Tag", " --- Making beacons query");
                retCursor = db.query(
                        BtrackerContract.BeaconsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                retCursor.setNotificationUri(resolver, BtrackerContract.BeaconsEntry.CONTENT_URI);
                break;
            default:
                throw new IllegalArgumentException("URI is not supported: " + uri + " " + sUriMatcher.match(uri));
        }

        //retCursor.setNotificationUri(resolver, uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case BEACONS:
                return BtrackerContract.BeaconsEntry.CONTENT_TYPE;
            case ITEM_BEACONS:
                return BtrackerContract.BeaconsEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = openHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;


        switch (match) {
            case BEACONS:

                long _id = db.insert(BtrackerContract.BeaconsEntry.TABLE_NAME, null, values);

                if (_id > 0)
                    returnUri = BtrackerContract.BeaconsEntry.buildBeaconsUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            default:
                throw new IllegalArgumentException("URI is not supported: " + uri);
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
    public int bulkInsert(Uri uri, @NonNull ContentValues[] values) {
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
        /*matcher.addURI(authority, BtrackerContract.PATH_BEACONS, ITEM_BEACONS);
        matcher.addURI(authority, BtrackerContract.PATH_PRODUCTS, PRODUCTS);
        matcher.addURI(authority, BtrackerContract.PATH_PRODUCTS, ITEM_PRODUCTS);
        matcher.addURI(authority, BtrackerContract.PATH_PRODUCTS_ZONES, PRODUCTS_ZONES);
        matcher.addURI(authority, BtrackerContract.PATH_PRODUCTS_ZONES, ITEM_PRODUCTS_ZONES);
        matcher.addURI(authority, BtrackerContract.PATH_CUSTOMERS, CUSTOMERS);
        matcher.addURI(authority, BtrackerContract.PATH_CUSTOMERS, ITEM_CUSTOMERS);
        matcher.addURI(authority, BtrackerContract.PATH_CUSTOMERS_PRODUCTS, CUSTOMERS_PRODUCTS);
        matcher.addURI(authority, BtrackerContract.PATH_CUSTOMERS_PRODUCTS, ITEM_CUSTOMERS_PRODUCTS);
        matcher.addURI(authority, BtrackerContract.PATH_PURCHASES, PURCHASES);
        matcher.addURI(authority, BtrackerContract.PATH_PURCHASES, ITEM_PURCHASES);
        matcher.addURI(authority, BtrackerContract.PATH_VISITS, VISITS);
        matcher.addURI(authority, BtrackerContract.PATH_VISITS, ITEM_VISITS);
        matcher.addURI(authority, BtrackerContract.PATH_STORES, STORES);
        matcher.addURI(authority, BtrackerContract.PATH_STORES, ITEM_STORES);
        matcher.addURI(authority, BtrackerContract.PATH_ZONES, ZONES);
        matcher.addURI(authority, BtrackerContract.PATH_ZONES, ITEM_ZONES);*/

        //TODO: add matcher for every URI
        return matcher;
    }
}
