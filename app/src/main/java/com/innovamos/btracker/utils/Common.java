package com.innovamos.btracker.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {

    public static long UnixTime() {
        long unixTime = System.currentTimeMillis() / 1000L;
        return unixTime;
    }
}
