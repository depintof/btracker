package com.innovamos.btracker;

/**
 * Principal BeaconDTO finder
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
import com.innovamos.btracker.async.EventListener;
import com.innovamos.btracker.dto.BeaconDTO;
import com.innovamos.btracker.json.JsonResponseDecoder;

import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

public class MyApplication extends Application implements EventListener {

    private BeaconManager beaconManager;
    private Beacon nearestBeacon;
    private Region region;

    @Override
    public void onCreate() {
        super.onCreate();

        beaconManager = new BeaconManager(getApplicationContext());
        beaconManager.setBackgroundScanPeriod(1500, 1000);
        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                if (!list.isEmpty()) {
                    nearestBeacon = list.get(0);
                    Log.e("Beacon notificacion: ", nearestBeacon.getMacAddress().toString());

                    showNotification(
                            "Promoción encontrada!",
                            "Toca para ver detalles"
                    );
                }
            }

            @Override
            public void onExitedRegion(Region region) {
                showNotification(
                        "Hasta luego!",
                        "Recuerda volver para más descuentos");
            }
        });

        region = new Region("ranged region", UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(region);
            }
        });

        // Obtener listado de Beacons
        //DatabaseConnectivity databaseConnectivity = new DatabaseConnectivity(this);
        //databaseConnectivity.getBeaconsList(this);
    }

    public void showNotification(String title, String message) {
        Intent notifyIntent = new Intent(this, ProductActivity.class);
        //notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        notifyIntent.putExtra("ProductBeacon", nearestBeacon);

        //PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
        //        new Intent[]{notifyIntent}, PendingIntent.FLAG_CANCEL_CURRENT);
        int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, uniqueInt,
                notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
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

    @Override
    public void beaconsListResult(JSONObject jsonResult) {
        final BeaconDTO[] beaconDTOList = JsonResponseDecoder.beaconListResponse(jsonResult);
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                if (beaconDTOList != null) {
                    for (BeaconDTO iteratorBeaconDTO : beaconDTOList)
                        if (iteratorBeaconDTO.getUuid().equals("B9407F30-F5F8-466E-AFF9-25556B57FE6D")) {
                            beaconManager.startMonitoring(new Region("BeaconDTO " + iteratorBeaconDTO.getId(),
                                    UUID.fromString(iteratorBeaconDTO.getUuid()), Integer.parseInt(iteratorBeaconDTO.getMajor()), Integer.parseInt(iteratorBeaconDTO.getMinor())));
                        }
                }
            }
        });
    }

    @Override
    public void customerResult(JSONObject jsonResult) {

    }

    @Override
    public void zoneResult(JSONObject jsonResult) {

    }

    @Override
    public void productsZoneList(JSONObject jsonResult) {

    }

    @Override
    public void productsLikeList(JSONObject jsonResult) {

    }

    @Override
    public void insertProductLike(JSONObject jsonResult) {

    }

    @Override
    public void deleteProductLike(JSONObject jsonResult) {

    }

    @Override
    public void purchasedProductsList(JSONObject jsonResult) {

    }

    @Override
    public void insertProductPurchase(JSONObject jsonResult) {

    }

    @Override
    public void deleteProductPurchase(JSONObject jsonResult) {

    }

    @Override
    public void customerVisitsList(JSONObject jsonResult) {

    }

    @Override
    public void customerNotificationsList(JSONObject jsonResult) {

    }
}
