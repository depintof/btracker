package com.btracker.test9;

/**
 * Principal Beacon finder
 */
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.List;
import java.util.UUID;

public class MyApplication extends Application {

    private BeaconManager beaconManager;
    private Beacon nearestBeacon;

    @Override
    public void onCreate() {
        super.onCreate();

        beaconManager = new BeaconManager(getApplicationContext());
        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                if (!list.isEmpty()) {
                    nearestBeacon = list.get(0);
                    Log.e("Beacon notificacion: ",nearestBeacon.getMacAddress().toString());
                }
                showNotification(
                        region.getIdentifier(),
                        "Current Beacon: "
                                + "MAC: " + nearestBeacon.getMacAddress().toString()
                                + "Minor: " + region.getMinor().toString());
            }
            @Override
            public void onExitedRegion(Region region) {
//                // could add an "exit" notification too if you want (-:
//                showNotification(
//                        "Bye bye little programer.",
//                        "Have an ass day!");
//
            }
        });
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(new Region("Beacon 1",
                        UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), 54167, 16064));
                beaconManager.startMonitoring(new Region("Beacon 2",
                        UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), 60906, 40046));
            }
        });
    }

    public void showNotification(String title, String message) {
        Intent notifyIntent = new Intent(this, ProductActivity.class);
        //notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        notifyIntent.putExtra("ProductBeacon",nearestBeacon);

        //PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
        //        new Intent[]{notifyIntent}, PendingIntent.FLAG_CANCEL_CURRENT);
        int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, uniqueInt,
                notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                //.setSmallIcon(android.R.drawable.ic_dialog_info)
                .setSmallIcon(R.drawable.ic_action_descuentos)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
}
