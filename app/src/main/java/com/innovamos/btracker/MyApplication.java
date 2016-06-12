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
import com.innovamos.btracker.dto.CustomerDTO;
import com.innovamos.btracker.dto.VisitsDTO;
import com.innovamos.btracker.dto.ZoneDTO;
import com.innovamos.btracker.json.JsonResponseDecoder;
import com.innovamos.btracker.utils.Common;
import com.innovamos.btracker.web.DatabaseConnectivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MyApplication extends Application implements EventListener {

    private CustomerDTO customerDTO;
    private BeaconManager beaconManager;
    private Beacon nearestBeacon;
    private Region region;
    List<BeaconDTO> beaconsList = new ArrayList<>();
    List<VisitsDTO> visitsList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();

        beaconManager = new BeaconManager(getApplicationContext());
        beaconManager.setBackgroundScanPeriod(1000,1000);
        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region identifiedRegion, List<Beacon> list) {
                if (!list.isEmpty()) {
                    nearestBeacon = list.get(0);
                    Log.e("Beacon notificacion: ", nearestBeacon.getMacAddress().toString());
                    addVisit(identifiedRegion, Common.UnixTime(), true);
                    showNotification(
                            "Promoción encontrada!",
                            "Toca para ver detalles"
                    );
                }
            }

            @Override
            public void onExitedRegion(Region identifiedRegion) {
                showNotification(
                        "Hasta luego!",
                        "Recuerda volver para más descuentos");
                addVisit(identifiedRegion, Common.UnixTime(), false);

            }
        });

        /*
        region = new Region("ranged region", UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), 60906, 40046);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(region);
            }
        });
        */

        // Obtener listado de Beacons
        DatabaseConnectivity databaseConnectivity = new DatabaseConnectivity(this);
        databaseConnectivity.getBeaconsList(this);
        databaseConnectivity.getCustomer(this);
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

    public void addVisit(Region identifiedRegion, long currentDate, boolean isEnteringToRegion){
        BeaconDTO beaconRegion = new BeaconDTO(null,identifiedRegion.getProximityUUID().toString(),identifiedRegion.getMajor().toString(),identifiedRegion.getMinor().toString(),null,null,null);
        if(beaconsList.contains(beaconRegion)){
            beaconRegion = beaconsList.get(beaconsList.indexOf(beaconRegion));
            DatabaseConnectivity databaseConnectivity = new DatabaseConnectivity(MyApplication.this);
            databaseConnectivity.getZoneVisit(MyApplication.this, beaconRegion.getId(), currentDate, isEnteringToRegion);
        }
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
        if(beaconDTOList!=null){
            for(BeaconDTO beacon: beaconDTOList){
                beaconsList.add(beacon);
            }
        }
    }

    @Override
    public void customerResult(JSONObject jsonResult) {
        customerDTO = JsonResponseDecoder.customerResponse(jsonResult);
    }

    @Override
    public void zoneResult(JSONObject jsonResult) {
     }

    @Override
    public void zoneVisitResult(JSONObject jsonResult, long currentDate, boolean isEnteringToRegion) {
        ZoneDTO zoneDTO = JsonResponseDecoder.zoneResponse(jsonResult);
        if(zoneDTO!=null){
            if(isEnteringToRegion) {
                VisitsDTO visitsDTO = new VisitsDTO(null,String.valueOf(currentDate),customerDTO.getId(),zoneDTO.getId());
                visitsList.add(visitsDTO);
            }
            else{
                VisitsDTO visitsDTO = new VisitsDTO(null,customerDTO.getId(),zoneDTO.getId());
                if(visitsList.contains(visitsDTO)){
                    visitsDTO.setTrigger_time(visitsList.get(visitsList.indexOf(visitsDTO)).getTrigger_time());
                    visitsDTO.setLeave_time(String.valueOf(currentDate));
                    visitsDTO.setViewed("0");
                    DatabaseConnectivity databaseConnectivity = new DatabaseConnectivity(MyApplication.this);
                    databaseConnectivity.createVisit(MyApplication.this,visitsDTO);
                }
            }
        }
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

    @Override
    public void insertVisit(JSONObject jsonResult) {
        VisitsDTO insertedVisit = JsonResponseDecoder.visitResponse(jsonResult);
        if(visitsList.contains(insertedVisit)){
            visitsList.remove(visitsList.indexOf(insertedVisit));
        }
    }
}
