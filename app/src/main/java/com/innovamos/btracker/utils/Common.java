package com.innovamos.btracker.utils;

import android.content.Context;
import android.util.Log;

import java.text.NumberFormat;
import java.util.Locale;

public class Common {

    public static long UnixTime() {
        return System.currentTimeMillis() / 1000L;
    }

    public static String FormatCurrency(Context context, Integer quantity) {
        Locale currentLocale = context.getResources().getConfiguration().locale;

        NumberFormat numberFormatter;
        numberFormatter = NumberFormat.getNumberInstance(currentLocale);
        String currency = numberFormatter.format(quantity);

        Log.d("Currency", currency);

        return currency;
    }

    public static String FormatCurrency(Context context, Double quantity) {
        Locale currentLocale = context.getResources().getConfiguration().locale;

        NumberFormat numberFormatter;
        numberFormatter = NumberFormat.getNumberInstance(currentLocale);
        String currency = numberFormatter.format(quantity);

        Log.d("Currency", currency);

        return currency;
    }

    public static double getDistance(int rssi, int txPower) {
        /*
         * RSSI = TxPower - 10 * n * lg(d)
         * n = 2 (in free space)
         *
         * d = 10 ^ ((TxPower - RSSI) / (10 * n))
         */

        return Math.pow(10d, ((double) txPower - rssi) / (10 * 2));
    }
}
