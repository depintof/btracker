package com.innovamos.btracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.innovamos.btracker.data.BtrackerContract.*;

/**
 * Created by root on 29/05/16.
 */
public class BtrackerDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    static final String DATABASE_NAME = "btracker.db";

    public BtrackerDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_BEACONS_TABLE = "CREATE TABLE " + BeaconsEntry.TABLE_NAME + " (" +
                BeaconsEntry.COLUMN_ID + " int(11) NOT NULL, " +
                BeaconsEntry.COLUMN_UUID + " varchar(100) NOT NULL, " +
                BeaconsEntry.COLUMN_MAJOR + " int(11) NOT NULL, " +
                BeaconsEntry.COLUMN_NAME + " varchar(45) DEFAULT NULL, " +
                BeaconsEntry.COLUMN_MINOR + " int(11) NOT NULL, " +
                BeaconsEntry.COLUMN_DETECTION_RANGE + " int(11) NOT NULL, " +
                BeaconsEntry.COLUMN_CREATED + " datetime DEFAULT NULL, " +
                BeaconsEntry.COLUMN_MODIFIED + " datetime DEFAULT NULL );";

        final String SQL_CREATE_CUSTOMERS_TABLE = "CREATE TABLE " + CustomersEntry.TABLE_NAME + " (" +
                CustomersEntry.COLUMN_ID + " int(11)NOT NULL, " +
                CustomersEntry.COLUMN_MAC + " varchar(45)NOT NULL, " +
                CustomersEntry.COLUMN_CREATED + " datetime DEFAULT NULL, " +
                CustomersEntry.COLUMN_MODIFIED + " datetime DEFAULT NULL );";

        final String SQL_CREATE_CUSTOMERS_PRODUCTS_TABLE = "CREATE TABLE " + CustomersProductsEntry.TABLE_NAME + " (" +
                CustomersProductsEntry.COLUMN_CUSTOMER_ID + " int(11)NOT NULL, " +
                CustomersProductsEntry.COLUMN_PRODUCT_ID + " int(11) NOT NULL);";

        final String SQL_CREATE_PRODUCTS_TABLE = "CREATE TABLE " + ProductsEntry.TABLE_NAME + " (" +
                ProductsEntry.COLUMN_ID + " int(11) NOT NULL, " +
                ProductsEntry.COLUMN_NAME + " varchar (45) NOT NULL, " +
                ProductsEntry.COLUMN_DESCRIPTION + " tinytext, " +
                ProductsEntry.COLUMN_PRICE + " decimal (10, 0)DEFAULT NULL, " +
                ProductsEntry.COLUMN_DISCOUNT + " decimal (10, 0)DEFAULT NULL, " +
                ProductsEntry.COLUMN_TERMS + " longtext, " +
                ProductsEntry.COLUMN_PICTURE + " varchar (255) DEFAULT NULL, " +
                ProductsEntry.COLUMN_PICTURE_DIR + " varchar (255) DEFAULT NULL, " +
                ProductsEntry.COLUMN_STATUS + " tinyint (1) NOT NULL DEFAULT '1', " +
                ProductsEntry.COLUMN_CREATED + " datetime DEFAULT NULL, " +
                ProductsEntry.COLUMN_MODIFIED + " datetime DEFAULT NULL, " +
                ProductsEntry.COLUMN_TYPE + " varchar(45) NOT NULL );";


        final String SQL_CREATE_PRODUCTS_ZONES_TABLE = "CREATE TABLE " + ProductsZonesEntry.TABLE_NAME + " (" +
                ProductsZonesEntry.COLUMN_ZONE_ID + " int(11)NOT NULL, " +
                ProductsZonesEntry.COLUMN_PRODUCT_ID + " int(11)NOT NULL );";

        final String SQL_CREATE_PURCHASES_TABLE = "CREATE TABLE " + PurchasesEntry.TABLE_NAME + " (" +
                PurchasesEntry.COLUMN_ID + " int(11)NOT NULL, " +
                PurchasesEntry.COLUMN_PRODUCT_ID + " int(11)NOT NULL, " +
                PurchasesEntry.COLUMN_CUSTOMER_ID + " int(11) NOT NULL, " +
                PurchasesEntry.COLUMN_DATE + " datetime NOT NULL, " +
                PurchasesEntry.COLUMN_PRICE + " decimal (10, 0)DEFAULT '0' );";

        final String SQL_CREATE_STORES_TABLE = "CREATE TABLE " + StoresEntry.TABLE_NAME + " (" +
                StoresEntry.COLUMN_ID + " int(11)NOT NULL, " +
                StoresEntry.COLUMN_USER_ID + " int(11)NOT NULL, " +
                StoresEntry.COLUMN_NAME + " varchar(45)NOT NULL, " +
                StoresEntry.COLUMN_DESCRIPTION + " tinytext, " +
                StoresEntry.COLUMN_STATUS + " tinyint(1)NOT NULL DEFAULT '1', " +
                StoresEntry.COLUMN_CREATED + " varchar(45) NOT NULL, " +
                StoresEntry.COLUMN_MODIFIED + " varchar (45) NOT NULL);";

        final String SQL_CREATE_VISITS_TABLE = "CREATE TABLE " + VisitsEntry.TABLE_NAME + " (" +
                VisitsEntry.COLUMN_ID + " int(11)NOT NULL, " +
                VisitsEntry.COLUMN_TRIGGER_TIME + " datetime NOT NULL, " +
                VisitsEntry.COLUMN_LEAVE_TIME + " datetime DEFAULT NULL, " +
                VisitsEntry.COLUMN_CUSTOMER_ID + " int(11)NOT NULL, " +
                VisitsEntry.COLUMN_ZONE_ID + " int(11) NOT NULL, " +
                VisitsEntry.COLUMN_VIEWED + " tinyint (1) DEFAULT '0' );";

        final String SQL_CREATE_ZONES_TABLE = "CREATE TABLE " + ZonesEntry.TABLE_NAME + " (" +
                ZonesEntry.COLUMN_ID + " int(11) NOT NULL, " +
                ZonesEntry.COLUMN_NAME + " varchar(45) NOT NULL, " +
                ZonesEntry.COLUMN_DESCRIPTION + " tinytext, " +
                ZonesEntry.COLUMN_STORE_ID + " int(11) NOT NULL, " +
                ZonesEntry.COLUMN_BEACON_ID + " int(11) DEFAULT NULL, " +
                ZonesEntry.COLUMN_CREATED + " datetime DEFAULT NULL, " +
                ZonesEntry.COLUMN_MODIFIED + " datetime DEFAULT NULL, " +
                ZonesEntry.COLUMN_ENTRANCE + " tinyint(1) DEFAULT NULL, " +
                ZonesEntry.COLUMN_STATUS + " tinyint(1) DEFAULT '1' );";

        db.execSQL(SQL_CREATE_BEACONS_TABLE);
        db.execSQL(SQL_CREATE_CUSTOMERS_TABLE);
        db.execSQL(SQL_CREATE_CUSTOMERS_PRODUCTS_TABLE);
        db.execSQL(SQL_CREATE_PRODUCTS_TABLE);
        db.execSQL(SQL_CREATE_PRODUCTS_ZONES_TABLE);
        db.execSQL(SQL_CREATE_PURCHASES_TABLE);
        db.execSQL(SQL_CREATE_STORES_TABLE);
        db.execSQL(SQL_CREATE_VISITS_TABLE);
        db.execSQL(SQL_CREATE_ZONES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
